package com.interviews.req10079266.dao.cassandra;

import com.interviews.req10079266.dao.Chunk;
import com.interviews.req10079266.dao.IncidentDAO;
import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.data.IncidentData;
import com.interviews.req10079266.entities.cassandra.DailyIncidentSummary;
import com.interviews.req10079266.entities.cassandra.DayIncident;
import com.interviews.req10079266.exceptions.DataLoadingException;
import com.interviews.req10079266.exceptions.DataSavingException;
import com.interviews.req10079266.utils.CassandraEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("local-cassandra")
@Slf4j
public class CassandraDAO implements IncidentDAO {

    private DayIncidentRepository dayIncidentRepository;
    private DailyIncidentSummaryRepository dailyIncidentSummaryRepository;
    private CassandraEntityMapper entityMapper;
    @Value("${app.config.cassandra.page-size}")
    private int pageSize;

    @Autowired
    public CassandraDAO(
            DayIncidentRepository dayIncidentRepository,
            DailyIncidentSummaryRepository dailyIncidentSummaryRepository,
            CassandraEntityMapper entityMapper) {
        this.dayIncidentRepository = dayIncidentRepository;
        this.dailyIncidentSummaryRepository = dailyIncidentSummaryRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Chunk<IncidentData> loadIncidentsByDay(LocalDate day) {
        log.info("Loading incidents registered on day: {}", day);
        try {
            Slice<DayIncident> slice = loadIncidentsByDay(day, CassandraPageRequest.first(pageSize));
            return buildChunk(slice);
        } catch (Exception e) {
            log.error("An error occurred when loading the incidents of day {}: {}", day, ExceptionUtils.getStackTrace(e));
            throw new DataLoadingException(e);
        }
    }

    @Override
    public Chunk<IncidentData> loadIncidentsByDay(LocalDate day, Chunk<IncidentData> previousChunk) {
        log.info("Loading next page of incidents registered on day: {}", day);
        try {
            Slice<DayIncident> slice = loadIncidentsByDay(day, CassandraPageRequest.of(previousChunk.getNextChunk(), pageSize));
            return buildChunk(slice);
        } catch (Exception e) {
            log.error("An error occurred when loading next page of the incidents of day {}: {}", day, ExceptionUtils.getStackTrace(e));
            throw new DataLoadingException(e);
        }
    }

    @Override
    public void saveDailyIncidentSummary(DailyIncidentSummaryData data) {
        log.info("Saving daily incident summary of day: {}", data.getDate());
        try {
            List<DailyIncidentSummary> entities = data.getAssetSummaries().stream()
                    .map(dto -> entityMapper.map(dto, data.getDate())).collect(Collectors.toList());
            dailyIncidentSummaryRepository.saveAll(entities);
        } catch (Exception e) {
            log.error("An error occurred when saving the daily incident summary of day {}: {}", data.getDate(), ExceptionUtils.getStackTrace(e));
            throw new DataSavingException(e);
        }
    }

    @Override
    public DailyIncidentSummaryData loadDailyIncidentSummary(LocalDate day) {
        log.info("Loading daily incident summaries of day: {}", day);
        try {
            List<DailyIncidentSummary> summaries = dailyIncidentSummaryRepository.findByDayOfOccurrence(day);
            DailyIncidentSummaryData summaryData = new DailyIncidentSummaryData();
            summaryData.setDate(day);
            summaryData.setAssetSummaries(summaries.stream().map(entityMapper::map).collect(Collectors.toList()));
            return summaryData;
        } catch (Exception e) {
            log.error("An error occurred when loading daily incident summaries of day {}: {}", day, ExceptionUtils.getStackTrace(e));
            throw new DataLoadingException(e);
        }
    }

    private Slice<DayIncident> loadIncidentsByDay(LocalDate day, CassandraPageRequest cassandraPageRequest) {
        LocalDateTime start = day.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return dayIncidentRepository.findByDayOfOccurrence(start, end, cassandraPageRequest);
    }

    private Chunk<IncidentData> buildChunk(Slice<DayIncident> slice) {
        List<IncidentData> data = slice.getContent().stream().map(entityMapper::map).collect(Collectors.toList());
        return new Chunk<>(data, slice.getNumberOfElements(), slice.hasNext() ? slice.getSize() : -1);
    }
}
