package com.interviews.req10079266.services;

import com.interviews.req10079266.exceptions.DataLoadingException;
import com.interviews.req10079266.exceptions.DataSavingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class IncidentSummaryScheduler {

    private IncidentReporter incidentReporter;

    @Autowired
    public IncidentSummaryScheduler(IncidentReporter incidentReporter) {
        this.incidentReporter = incidentReporter;
    }

    @Scheduled(cron = "${app.config.daily-summary-schedule}")
    public void generateIncidentSummary() {
        LocalDate today = LocalDate.now();
        log.info("Triggering scheduled incident report for day: {}", today);
        try {
            incidentReporter.generateIncidentDailyReport(today);
            log.info("Finished scheduled incident report for day: {}", today);
        } catch (DataLoadingException | DataSavingException e) {
            log.error("An error occurred when retrieving/persisting the incident report of day {}: {}", today, ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            log.error("An unexpected error occurred when generationg the incident report of day {}: {}", today, ExceptionUtils.getStackTrace(e));
        }
    }
}
