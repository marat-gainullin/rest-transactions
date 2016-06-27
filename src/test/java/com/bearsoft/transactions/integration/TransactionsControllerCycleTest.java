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

/**
 * Test suite for cycles in transactions tree detection.
 *
 * @author mg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TransactionsControllerCycleTest.Config.class}, loader = AnnotationConfigContextLoader.class)
public class TransactionsControllerCycleTest
        extends AbstractJUnit4SpringContextTests {

    /**
     * Configuration stub for spring <code>SpringJUnit4ClassRunner</code>
     *
     * @see SpringJUnit4ClassRunner
     */
    @Configuration
    public static class Config extends TransactionsTestConfig {
    }

    /**
     * A <code>TransactionsController</code> autowired instance.
     *
     * @see TransactionsController
     */
    @Autowired
    private TransactionsController transactionController;

    /**
     * Spring's test mockery.
     *
     * @see MockMvc
     */
    private MockMvc mockMvc;

    /**
     * Setup of Spring's mockery.
     */
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .build();
    }

    /**
     * Cycles detection test.
     *
     * @throws Exception
     */
    @Test
    public void cycleTest() throws Exception {
        mockMvc.perform(put("/transaction/{transaction-id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 7000"
                        + ", \"type\": \"fruit\", \"parent_id\": 2 }")
        ).andExpect(status().isOk());
        mockMvc.perform(put("/transaction/{transaction-id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 2000"
                        + ", \"type\": \"fruit\", \"parent_id\": 3}")
        ).andExpect(status().isOk());
        mockMvc.perform(put("/transaction/{transaction-id}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 3000"
                        + ", \"type\": \"fruit\", \"parent_id\": 1} }")
        ).andExpect(status().isOk());
        mockMvc.perform(get("/sum/{transaction-id}", 1))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\": "
                        + "\"Cycle in transactions tree with transaction id "
                        + 1 + " detected\"}"));
    }
}
