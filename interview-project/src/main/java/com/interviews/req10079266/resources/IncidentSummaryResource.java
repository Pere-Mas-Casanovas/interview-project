package com.interviews.req10079266.resources;

import com.interviews.req10079266.dao.IncidentDAO;
import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/incidents")
@Slf4j
public class IncidentSummaryResource {

    private AppUtils appUtils;
    private IncidentDAO incidentDAO;

    @Autowired
    public IncidentSummaryResource(AppUtils appUtils, IncidentDAO incidentDAO) {
        this.appUtils = appUtils;
        this.incidentDAO = incidentDAO;
    }

    @GetMapping("/daily-summary/{input-date}")
    public ResponseEntity<byte[]> dailySummary(@PathVariable("input-date") String inputDate) throws IOException {
        log.info("Received request to download incidents summary of day: {}", inputDate);
        LocalDate summaryDate = appUtils.checkInputDateFormat(inputDate);
        DailyIncidentSummaryData dailySummary = incidentDAO.loadDailyIncidentSummary(summaryDate);
        if (dailySummary == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .headers(appUtils.buildHeaderForCSVOutput(summaryDate))
                .body(appUtils.buildCSVOutput(dailySummary));
    }
}
