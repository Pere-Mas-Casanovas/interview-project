package com.interviews.req10079266.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncidentData {
    private String assetName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int severity;
}
