package com.interviews.req10079266.dao.mock;

import com.interviews.req10079266.data.IncidentData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MockedDAOTest {

    @Autowired
    private MockedDAO incidentDAO;

    @Test
    public void shouldNormallyMapCSVPlainRowToIncidentData () {
        List<String> sampleRow = new ArrayList<>();
        sampleRow.add("CMI");
        sampleRow.add("4/3/2019 11:00");
        sampleRow.add("4/3/2019 11:10");
        sampleRow.add("1");
        IncidentData incidentData = incidentDAO.mapRow(sampleRow);
        Assertions.assertEquals("CMI", incidentData.getAssetName());
    }

    @Test
    public void shouldNormallyVerifyThatTheIncidentStartTimeIsWithinTheTargetDate() {
        List<String> sampleRow = new ArrayList<>();
        sampleRow.add("CMI");
        sampleRow.add("4/3/2019 11:00");
        sampleRow.add("4/3/2019 11:10");
        sampleRow.add("1");
        Assertions.assertTrue(incidentDAO.isOfDay(sampleRow, LocalDate.parse("4/3/2019", DateTimeFormatter.ofPattern("d/M/yyyy"))));
    }

    @Test
    public void shouldNormallyLoadIncidentsOfFourthOfJanuary2019() {
        List<IncidentData> result = incidentDAO.loadIncidentsByDay(LocalDate.parse("4/1/2019", DateTimeFormatter.ofPattern("d/M/yyyy"))).getRows();
        Assertions.assertEquals(11, result.size());
    }
}
