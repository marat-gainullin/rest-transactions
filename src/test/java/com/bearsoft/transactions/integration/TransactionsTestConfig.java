package com.bearsoft.transactions.integration;

import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.model.TransactionsStore;
import com.bearsoft.transactions.services.TransactionsMemoryStoreBean;
import com.bearsoft.transactions.services.TransactionsProcessorBean;
import com.bearsoft.transactions.web.TransactionsController;
import org.springframework.context.annotation.Bean;

/**
 * Spring's beans source for tests.
 *
 * @author mg
 */
public class TransactionsTestConfig {

    /**
     * <code>TransactionsController</code> factory method.
     *
     * @return <code>TransactionsController</code> instance.
     */
    @Bean
    public TransactionsController transactionsController() {
        return new TransactionsController();
    }

    /**
     * <code>TransactionsStore</code> factory method.
     *
     * @return <code>TransactionsStore</code> instance.
     */
    @Bean
    public TransactionsStore transactionsStore() {
        return new TransactionsMemoryStoreBean();
    }

    /**
     * <code>TransactionsProcessor</code> factory method.
     *
     * @return <code>TransactionsProcessor</code> instance.
     */
    @Bean
    public TransactionsProcessor transactionsProcessor() {
        return new TransactionsProcessorBean();
    }
}
