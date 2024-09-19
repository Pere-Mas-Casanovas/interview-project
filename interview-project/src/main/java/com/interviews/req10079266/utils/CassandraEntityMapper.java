package com.interviews.req10079266.utils;

import com.interviews.req10079266.data.AssetDayIncidentSummaryData;
import com.interviews.req10079266.data.DailyIncidentSummaryData;
import com.interviews.req10079266.data.IncidentData;
import com.interviews.req10079266.entities.cassandra.DailyIncidentSummary;
import com.interviews.req10079266.entities.cassandra.DayIncident;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CassandraEntityMapper {

    private ModelMapper modelMapper;

    public CassandraEntityMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(DayIncident.class, IncidentData.class);
        modelMapper.createTypeMap(AssetDayIncidentSummaryData.class, DailyIncidentSummary.class);
        modelMapper.createTypeMap(DailyIncidentSummary.class, AssetDayIncidentSummaryData.class);
    }

    public IncidentData map(DayIncident dayIncident) {
        return modelMapper.map(dayIncident, IncidentData.class);
    }

    public DailyIncidentSummary map(AssetDayIncidentSummaryData assetData, LocalDate dayOfOcurrence) {
        DailyIncidentSummary summary = new DailyIncidentSummary(dayOfOcurrence);
        modelMapper.map(assetData, summary);
        return summary;
    }

    public AssetDayIncidentSummaryData map(DailyIncidentSummary dailyIncidentSummary) {
        return modelMapper.map(dailyIncidentSummary, AssetDayIncidentSummaryData.class);
    }

}
