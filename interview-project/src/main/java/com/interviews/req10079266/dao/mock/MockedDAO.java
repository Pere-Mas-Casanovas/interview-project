package com.interviews.req10079266.dao.mock;

import com.interviews.req10079266.dao.Chunk;
import com.interviews.req10079266.dao.IncidentDAO;
import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.data.IncidentData;
import com.interviews.req10079266.exceptions.DataLoadingException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("local-dev")
@Slf4j
public class MockedDAO implements IncidentDAO {

    private Map<LocalDate, DailyIncidentSummaryData> dailyIncidentSummaries = new HashMap<>();
    private List<List<String>> inputData = new ArrayList<>();

    @Value("${app.config.input-formats.plain-csv.date-time}")
    private String dateTimeFormat;

    static final String COMMA_DELIMITER = ",";

    @Autowired
    ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        Resource resource = resourceLoader.getResource("classpath:mocked/input.csv");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(resource.getFile().getPath()))) {
            this.inputData = reader.lines()
                    .map(line -> Arrays.asList(line.split(COMMA_DELIMITER)))
                    .collect(Collectors.toList());
            this.inputData = this.inputData.subList(1, inputData.size());
        } catch (IOException e) {
            // This is a mock. It should not fail
        }
    }

    @Override
    public Chunk<IncidentData> loadIncidentsByDay(LocalDate day) {
        log.info("Loading incidents registered on day: {}", day);
        try {
            List<IncidentData> rows =  inputData.stream()
                    .filter(row -> isOfDay(row, day))
                    .map(this::mapRow)
                    .collect(Collectors.toList());
            return new Chunk<>(rows, rows.size(), -1);
        } catch (DateTimeParseException | NumberFormatException e) {
            log.error("An error occurred when parsing the input data of the incidents of day: {}", day, ExceptionUtils.getStackTrace(e));
            throw new DataLoadingException(e);
        } catch (Exception e) {
            log.error("An unexpected error occurred when loading the incidents of day {}: {}", day, ExceptionUtils.getStackTrace(e));
            throw new DataLoadingException(e);
        }
    }

    @Override
    public Chunk<IncidentData> loadIncidentsByDay(LocalDate day, Chunk<IncidentData> incidents) {
        // Because this is a mock, we don't need to implement a real pagination
        return null;
    }

    @Override
    public void saveDailyIncidentSummary(DailyIncidentSummaryData data) {
        synchronized (dailyIncidentSummaries) {
            this.dailyIncidentSummaries.put(data.getDate(), data);
        }
    }

    @Override
    public DailyIncidentSummaryData loadDailyIncidentSummary(LocalDate date) {
        return this.dailyIncidentSummaries.get(date);
    }

    boolean isOfDay(List<String> row, LocalDate targetDate) {
        LocalDateTime startTime = LocalDateTime.parse(row.get(1).trim(), DateTimeFormatter.ofPattern(dateTimeFormat));
        return targetDate.isEqual(startTime.toLocalDate());
    }

    IncidentData mapRow(List<String> row) {
        String assetName = row.get(0);
        LocalDateTime startTime = LocalDateTime.parse(row.get(1).trim(), DateTimeFormatter.ofPattern(dateTimeFormat));
        LocalDateTime endTime = LocalDateTime.parse(row.get(2).trim(), DateTimeFormatter.ofPattern(dateTimeFormat));
        int severity = Integer.parseInt(row.get(3));
        return new IncidentData(assetName, startTime, endTime, severity);
    }
}
