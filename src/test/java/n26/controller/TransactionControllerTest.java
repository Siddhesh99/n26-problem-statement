package n26.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import n26.model.Transaction;
import n26.service.StatisticsService;
import n26.util.TimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TimeUtils timeUtils;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void shouldReturnCreatedResponseForTransactionWithTimestampEarlierThan60SecondsFromNow() throws Exception {
        when(timeUtils.nowInMilliSeconds()).thenReturn(120000L);
        Transaction transaction = new Transaction(13.2, 110000L);

        mockMvc.perform(
                post("/transactions")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(transaction))
        ).andExpect(status().isCreated());

        verify(statisticsService).collect(eq(transaction));
    }

    @Test
    public void shouldReturnNoContentResponseForTimestampOlderThan60SecondsFromNow() throws Exception {
        when(timeUtils.nowInMilliSeconds()).thenReturn(200000L);
        Transaction invalidTransaction = new Transaction(13.2, 110000L);

        mockMvc.perform(
                post("/transactions")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(invalidTransaction))
        ).andExpect(status().isNoContent());
    }
}