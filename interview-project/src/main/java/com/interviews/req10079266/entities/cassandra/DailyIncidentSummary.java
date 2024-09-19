package com.interviews.req10079266.entities.cassandra;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("daily_incident_summary")
@Data
@NoArgsConstructor
public class DailyIncidentSummary {
    @PrimaryKey
    private UUID summaryId;
    private String assetName;
    private LocalDate dayOfOccurrence;
    private Long totalIncidents;
    private Long totalUptime;
    private Long rating;

    public DailyIncidentSummary(LocalDate dayOfOccurrence) {
        this.dayOfOccurrence = dayOfOccurrence;
    }
}
