package n26.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import n26.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void shouldReturnCreatedResponseForTransactionPost() throws Exception {
        Transaction validTransaction = new Transaction(125.0, 100000L);

        mockMvc.perform(
                post("/transactions")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(validTransaction))
        ).andExpect(status().isCreated());
    }
}