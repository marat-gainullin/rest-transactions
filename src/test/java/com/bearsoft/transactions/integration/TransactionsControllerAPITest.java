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
 * Test suite for <code>TransactionsController</code>.
 *
 * @see TransactionsController
 * @author mg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TransactionsControllerAPITest.Config.class},
        loader = AnnotationConfigContextLoader.class)
public class TransactionsControllerAPITest
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
     * Tests get put capabilities of <code>TransactionsController</code>.
     *
     * @see TransactionsController
     * @throws Exception
     */
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
                .andExpect(content()
                        .json("{\"parent_id\":null,\"amount\":5000.0"
                                + ",\"type\":\"fruit\"}"));
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

    @Test
    public void sumTest() throws Exception {
        mockMvc.perform(put("/transaction/{transaction-id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 3000, \"type\": \"washing\" }")
        ).andExpect(status().isOk());
        mockMvc.perform(put("/transaction/{transaction-id}", 6)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 7000, \"type\": \"washing\" }")
        ).andExpect(status().isOk());
        mockMvc.perform(get("/sum/{transaction-id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{sum: 3000}"));
        mockMvc.perform(get("/sum/{transaction-id}", 6))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{sum: 7000}"));
    }

    @Test
    public void deepSumTest() throws Exception {
        mockMvc.perform(put("/transaction/{transaction-id}", 7)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 3000, \"type\": \"washing\" }")
        ).andExpect(status().isOk());
        mockMvc.perform(put("/transaction/{transaction-id}", 8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 2000, \"type\": \"washing\""
                        + ", \"parent_id\": 7}")
        ).andExpect(status().isOk());
        mockMvc.perform(get("/sum/{transaction-id}", 7))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{sum: 5000}"));
    }

}
