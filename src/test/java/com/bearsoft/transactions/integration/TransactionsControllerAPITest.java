package com.bearsoft.transactions.integration;

import com.bearsoft.transactions.web.TransactionsController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TransactionsControllerAPITest.Config.class}, loader = AnnotationConfigContextLoader.class)
public class TransactionsControllerAPITest extends AbstractJUnit4SpringContextTests {

    @Configuration
    public static class Config extends TransactionsTestConfig {
    }

    @Autowired
    private TransactionsController transactionController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .build();
    }

    @Test
    public void putGetTest() throws Exception {
        mockMvc.perform(put("/transaction/{transaction-id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 5000, \"type\": \"fruit\" }")
        ).andExpect(status().isOk());
        mockMvc.perform(put("/transaction/{transaction-id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 5500, \"type\": \"_fruit_\" }")
        ).andExpect(status().isConflict());
        mockMvc.perform(get("/transaction/{transaction-id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"parent_id\":null,\"amount\":5000.0,\"type\":\"fruit\"}"));
    }
    
    @Test
    public void typesTest() throws Exception {
        mockMvc.perform(put("/transaction/{transaction-id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 3000, \"type\": \"shopping\" }")
        ).andExpect(status().isOk());
        mockMvc.perform(put("/transaction/{transaction-id}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 7000, \"type\": \"shopping\" }")
        ).andExpect(status().isOk());
        mockMvc.perform(put("/transaction/{transaction-id}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 7000, \"type\": \"backery\" }")
        ).andExpect(status().isOk());
        mockMvc.perform(get("/types/{type-name}", "shopping"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[2, 3]"));
        mockMvc.perform(get("/types/{type-name}", "backery"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[4]"));
    }

}
