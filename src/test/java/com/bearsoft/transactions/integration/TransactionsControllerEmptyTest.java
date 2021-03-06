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
 * Test suite for operations on a <code>TransactionsController</code> with empty
 * transaction store.
 *
 * @author mg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TransactionsControllerEmptyTest.Config.class},
        loader = AnnotationConfigContextLoader.class)
public class TransactionsControllerEmptyTest
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
     * Ensures, that empty store leads to valid result of empty JSON array.
     *
     * @throws Exception
     */
    @Test
    public void absentTypesTest() throws Exception {
        mockMvc.perform(get("/types/{type-name}", "backery"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    /**
     * Ensures, that empty store leads to valid result of correct error message
     * wrapped into JSON object.
     *
     * @throws Exception
     */
    @Test
    public void absentTransactionTest() throws Exception {
        long absentId = 10;
        mockMvc.perform(get("/transaction/{transaction-id}", absentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(""
                        + "{\"status\": \"Transaction with id "
                        + absentId + " is not found\"}"));
    }
}
