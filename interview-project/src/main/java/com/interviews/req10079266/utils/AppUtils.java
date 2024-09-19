package com.interviews.req10079266.utils;

import com.interviews.req10079266.data.AssetDayIncidentSummaryData;
import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.exceptions.InvalidInputDateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class AppUtils {

    public static final long SECONDS_IN_DAY = 86400;
    public static final String[] CSV_OUTPUT_HEADERS = new String[] {"Asset Name", "Total Incidents", "Total Downtime", "Rating"};

    public enum SeverityLevels {
        HIGH(1, 30), MEDIUM(2, 10), LOW(3, 10);

        @Getter
        private int val;
        @Getter
        private int weight;

        SeverityLevels(int val, int weight) {
            this.val = val;
            this.weight = weight;
        }

        public static long getWeightBySeverity(int severity) {
            switch (severity) {
                case 1: return HIGH.weight;
                case 2: return MEDIUM.weight;
                case 3: return LOW.weight;
            }
            return 0;
        }
    }

    @Value("${app.config.input-date-format}")
    private String inputDateFormat;

    public LocalDate checkInputDateFormat(String inputDate) {
        try {
            return LocalDate.parse(inputDate, DateTimeFormatter.ofPattern(inputDateFormat));
        } catch (Exception ex) {
            log.error("The provided input date is invalid: {}", inputDate, ex);
            throw new InvalidInputDateException(inputDate);
        }
    }

    public HttpHeaders buildHeaderForCSVOutput(LocalDate inputDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/vnd.ms-excel");
        headers.set("Content-Disposition", String.format("attachment; filename=\"output%s.csv\"", inputDate));
        return headers;
    }

    public byte[] buildCSVOutput(DailyIncidentSummaryData dailyIncidentSummaryData) throws IOException {
        StringWriter sw = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(CSV_OUTPUT_HEADERS)
                .build();
        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
            for (AssetDayIncidentSummaryData assetSummary: dailyIncidentSummaryData.getAssetSummaries()) {
                printer.printRecord(
                        assetSummary.getAssetName(),
                        assetSummary.getTotalIncidents(),
                        (SECONDS_IN_DAY - assetSummary.getTotalUptime()),
                        assetSummary.getRating());
            }
        }
        return sw.toString().getBytes();
    }
}
