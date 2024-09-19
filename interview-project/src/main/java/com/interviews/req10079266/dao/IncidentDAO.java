package com.interviews.req10079266.dao;

import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.data.IncidentData;

import java.time.LocalDate;

public interface IncidentDAO {
    Chunk<IncidentData> loadIncidentsByDay(LocalDate day);
    Chunk<IncidentData> loadIncidentsByDay(LocalDate day, Chunk<IncidentData> incidents);
    void saveDailyIncidentSummary(DailyIncidentSummaryData data);
    DailyIncidentSummaryData loadDailyIncidentSummary(LocalDate day);
}
