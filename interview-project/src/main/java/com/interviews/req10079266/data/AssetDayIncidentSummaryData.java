package com.interviews.req10079266.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssetDayIncidentSummaryData {
    private String assetName;
    private long totalIncidents;
    private long totalUptime;
    private long rating;
}
