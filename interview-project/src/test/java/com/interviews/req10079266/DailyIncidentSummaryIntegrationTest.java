package com.interviews.req10079266;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = {App.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles( value = "local-dev")
@AutoConfigureMockMvc
public class DailyIncidentSummaryIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldNormallyReturnA204BecauseThereIsNoIncidentesForTheProvidedDate() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/incidents/daily-summary/2024-09-20"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

    @Test
    public void shouldNormallyReturnA400BecauseTheInputDateFormatIsWrong() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/incidents/daily-summary/wrong-date"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    public void shouldNormallyReturnSummaryIncidentForTheProvidedDate() throws Exception {
        MvcResult result = mvc.perform(
                        MockMvcRequestBuilders.get("/incidents/daily-summary/2019-02-04"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Payments Gateway,3,2940,50"));
    }
}
