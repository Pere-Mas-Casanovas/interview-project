package com.interviews.req10079266.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyIncidentSummaryData {
    private LocalDate date;
    private List<AssetDayIncidentSummaryData> assetSummaries;
}
