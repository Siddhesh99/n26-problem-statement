package n26.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import n26.model.Statistics;
import n26.service.StatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {
    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
    }

    @Test
    public void shouldReturnDefaultStatisticsWithDefaultValues() throws Exception {
        Statistics defaultSummary = new Statistics(0.0, 0.0, 0.0, 0.0, 0L);
        when(statisticsService.getStatistics()).thenReturn(defaultSummary);

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(defaultSummary)));
    }

    @Test
    public void shouldReturnStatisticsSummaryWithActualValuesOfIndividualProperties() throws Exception {
        Statistics summary = new Statistics(600.0, 200.0, 200.0, 200.0, 3L);
        when(statisticsService.getStatistics()).thenReturn(summary);

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(summary)));
    }


}