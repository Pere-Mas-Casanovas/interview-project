package com.interviews.req10079266.services;

import com.interviews.req10079266.dao.Chunk;
import com.interviews.req10079266.dao.IncidentDAO;
import com.interviews.req10079266.data.AssetDayIncidentSummaryData;
import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.data.IncidentData;
import com.interviews.req10079266.utils.AppUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles( value = "local-dev")
@ExtendWith(MockitoExtension.class)
public class IncidentReporterTest {

    @InjectMocks
    private IncidentReporter incidentReporter;

    @Mock
    private IncidentDAO incidentDAO;

    @Test
    public void shouldNormallyAggregateIncidentDataToAssetDayIncidentsSummary() {
        IncidentData incident1 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 11:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 11:10", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        IncidentData incident2 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 12:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 12:10", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        IncidentData incident3 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 13:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 13:45", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.MEDIUM.getVal());

        List<IncidentData> incidentDataList = new ArrayList<>();
        incidentDataList.add(incident1);
        incidentDataList.add(incident2);
        incidentDataList.add(incident3);

        Map<String, AssetDayIncidentSummaryData> assetSummaries = new HashMap<>();

        incidentReporter.aggregateIncidentsData(incidentDataList, assetSummaries);
        Assertions.assertTrue(assetSummaries.containsKey("CMI"));
        AssetDayIncidentSummaryData assetData = assetSummaries.get("CMI");
        Assertions.assertEquals(3, assetData.getTotalIncidents());
        Assertions.assertEquals(85200, assetData.getTotalUptime());
        Assertions.assertEquals(70, assetData.getRating());
    }

    @Test
    public void shouldNormallySaveTheDailyIncidentSummary() {
        AssetDayIncidentSummaryData cmiData = new AssetDayIncidentSummaryData(
                "CMI",
                0,
                AppUtils.SECONDS_IN_DAY,
                0);
        AssetDayIncidentSummaryData mornlData = new AssetDayIncidentSummaryData(
                "MOR-NL",
                0,
                AppUtils.SECONDS_IN_DAY,
                0);
        Map<String, AssetDayIncidentSummaryData> assetSummaries = new HashMap<>();
        assetSummaries.put("CMI", cmiData);
        assetSummaries.put("MOR-NL", mornlData);
        incidentReporter.saveDailyIncidentSummary(
                LocalDate.parse("04-01-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                assetSummaries);
        Mockito.verify(incidentDAO, Mockito.times(1)).saveDailyIncidentSummary(Mockito.any(DailyIncidentSummaryData.class));
    }

    @Test
    public void shouldNormallyGenerateIncidentDailyReport() {
        IncidentData incident1 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 11:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 11:10", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        IncidentData incident2 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 11:45", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 11:48", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        IncidentData incident3 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 13:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 13:20", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.MEDIUM.getVal());

        IncidentData incident4 = new IncidentData(
                "MOR-NL",
                LocalDateTime.parse("4/1/2019 10:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 12:10", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        IncidentData incident5 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 15:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 15:05", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.MEDIUM.getVal());

        IncidentData incident6 = new IncidentData(
                "MOR-NL",
                LocalDateTime.parse("4/1/2019 12:30", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 13:05", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.LOW.getVal());

        IncidentData incident7 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 15:45", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 15:46", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        IncidentData incident8 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 17:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 18:05", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.LOW.getVal());

        IncidentData incident9 = new IncidentData(
                "SME-PO",
                LocalDateTime.parse("4/1/2019 00:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 23:59", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        IncidentData incident10 = new IncidentData(
                "CMI",
                LocalDateTime.parse("4/1/2019 20:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                LocalDateTime.parse("4/1/2019 20:05", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")),
                AppUtils.SeverityLevels.HIGH.getVal());

        List<IncidentData> incidentDataList = new ArrayList<>();
        incidentDataList.add(incident1);
        incidentDataList.add(incident2);
        incidentDataList.add(incident3);
        incidentDataList.add(incident4);
        incidentDataList.add(incident5);
        incidentDataList.add(incident6);
        incidentDataList.add(incident7);
        incidentDataList.add(incident8);
        incidentDataList.add(incident9);
        incidentDataList.add(incident10);

        ArgumentCaptor<DailyIncidentSummaryData> argumentCaptor = ArgumentCaptor.forClass(DailyIncidentSummaryData.class);
        Mockito.when(this.incidentDAO.loadIncidentsByDay(Mockito.any())).thenReturn(new Chunk<>(incidentDataList, incidentDataList.size(), -1));

        this.incidentReporter.generateIncidentDailyReport(LocalDate.parse("04-01-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        Mockito.verify(incidentDAO, Mockito.times(1)).loadIncidentsByDay(Mockito.any());
        Mockito.verify(incidentDAO, Mockito.times(1)).saveDailyIncidentSummary(argumentCaptor.capture());
        DailyIncidentSummaryData dailyIncidentSummaryData = argumentCaptor.getValue();
        Assertions.assertEquals(dailyIncidentSummaryData.getDate(), LocalDate.parse("04-01-2019", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Assertions.assertEquals(dailyIncidentSummaryData.getAssetSummaries().size(), 3);
        // TO DO Add more assertions
    }
}
