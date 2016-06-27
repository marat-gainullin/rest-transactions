package com.bearsoft.transactions.integration;

import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.model.TransactionsStore;
import com.bearsoft.transactions.services.TransactionsMemoryStoreBean;
import com.bearsoft.transactions.services.TransactionsProcessorBean;
import com.bearsoft.transactions.web.TransactionsController;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author mg
 */
public class TransactionsTestConfig {

    @Bean
    public TransactionsController transactionsController() {
        return new TransactionsController();
    }

    @Bean
    public TransactionsStore transactionsStore() {
        return new TransactionsMemoryStoreBean();
    }

    @Bean
    public TransactionsProcessor transactionsProcessor() {
        return new TransactionsProcessorBean();
    }
}
