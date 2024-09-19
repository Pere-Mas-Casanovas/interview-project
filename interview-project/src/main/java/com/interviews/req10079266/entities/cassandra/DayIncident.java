package com.interviews.req10079266.entities.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("day_incident")
@Data
public class DayIncident {
    @PrimaryKey
    private UUID incidentId;
    private String assetName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer severity;
}
