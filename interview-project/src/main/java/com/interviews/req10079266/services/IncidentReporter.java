package com.interviews.req10079266.services;

import com.interviews.req10079266.dao.Chunk;
import com.interviews.req10079266.dao.IncidentDAO;
import com.interviews.req10079266.data.AssetDayIncidentSummaryData;
import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.data.IncidentData;
import com.interviews.req10079266.utils.AppUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IncidentReporter {

    private IncidentDAO incidentDAO;

    @Autowired
    public IncidentReporter(IncidentDAO incidentDAO) {
        this.incidentDAO = incidentDAO;
    }

    public void generateIncidentDailyReport(LocalDate targetDay) {
        log.info("Generating daily incident report for the {}", targetDay);
        Chunk<IncidentData> chunk = incidentDAO.loadIncidentsByDay(targetDay);
        if (chunk.getTotalChunks() == 0) {
            log.info("No incidents found for the {}", targetDay);
            return;
        }
        Map<String, AssetDayIncidentSummaryData> assetSummaries = new HashMap<>();
        aggregateIncidentsData(chunk.getRows(), assetSummaries);
        while(chunk.getNextChunk() > 0) {
            chunk = incidentDAO.loadIncidentsByDay(targetDay, chunk);
            aggregateIncidentsData(chunk.getRows(), assetSummaries);
        }
        saveDailyIncidentSummary(targetDay, assetSummaries);
    }

    void aggregateIncidentsData(List<IncidentData> incidents, Map<String, AssetDayIncidentSummaryData> assetSummaries) {
        for (IncidentData incident : incidents) {
            if (!assetSummaries.containsKey(incident.getAssetName())) {
                log.info("Found incidents for asset {} in the provided day", incident.getAssetName());
                assetSummaries.put(incident.getAssetName(), new AssetDayIncidentSummaryData(incident.getAssetName(), 0, AppUtils.SECONDS_IN_DAY, 0));
            }
            AssetDayIncidentSummaryData currentAsset = assetSummaries.get(incident.getAssetName());
            currentAsset.setTotalIncidents(currentAsset.getTotalIncidents() + 1);
            if (incident.getSeverity() == AppUtils.SeverityLevels.HIGH.getVal()) {
                currentAsset.setTotalUptime(currentAsset.getTotalUptime() - Math.abs(
                        ChronoUnit.SECONDS.between(incident.getStartTime(), incident.getEndTime())));
            }
            currentAsset.setRating(currentAsset.getRating() + AppUtils.SeverityLevels.getWeightBySeverity(incident.getSeverity()));
        }
    }

    void saveDailyIncidentSummary(LocalDate summaryDay, Map<String, AssetDayIncidentSummaryData> assetSummaries) {
        log.info("Saving incident daily summary for day {}", summaryDay);
        DailyIncidentSummaryData dailySummaries = new DailyIncidentSummaryData();
        dailySummaries.setDate(summaryDay);
        dailySummaries.setAssetSummaries(assetSummaries.values().stream().collect(Collectors.toList()));
        incidentDAO.saveDailyIncidentSummary(dailySummaries);
    }
}
