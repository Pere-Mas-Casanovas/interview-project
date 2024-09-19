package com.interviews.req10079266.dao.cassandra;

import com.interviews.req10079266.entities.cassandra.DailyIncidentSummary;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DailyIncidentSummaryRepository extends CassandraRepository<DailyIncidentSummary, UUID> {
    List<DailyIncidentSummary> findByDayOfOccurrence(LocalDate dayOfOccurrence);
}
