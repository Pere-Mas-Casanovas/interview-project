package com.interviews.req10079266.services.mock;

import com.interviews.req10079266.services.IncidentReporter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Profile("local-dev")
@Slf4j
public class MockedOldReportsLoader {

    private IncidentReporter incidentReporter;

    @Autowired
    public MockedOldReportsLoader(IncidentReporter incidentReporter) {
        this.incidentReporter = incidentReporter;
    }

    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        incidentReporter.generateIncidentDailyReport(LocalDate.parse("2019-01-04", formatter));
        incidentReporter.generateIncidentDailyReport(LocalDate.parse("2019-02-04", formatter));
        incidentReporter.generateIncidentDailyReport(LocalDate.parse("2019-03-04", formatter));
        incidentReporter.generateIncidentDailyReport(LocalDate.parse("2019-04-04", formatter));
        incidentReporter.generateIncidentDailyReport(LocalDate.parse("2019-05-04", formatter));
    }
}
