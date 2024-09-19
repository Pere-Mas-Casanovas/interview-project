package com.interviews.req10079266.dao.cassandra;

import com.interviews.req10079266.entities.cassandra.DayIncident;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface DayIncidentRepository extends CassandraRepository<DayIncident, UUID> {

    @Query("select d from DayIncident where d.startTime > :start and d.endTime < :end")
    Slice<DayIncident> findByDayOfOccurrence(LocalDateTime start, LocalDateTime end, Pageable pageRequest);
}
