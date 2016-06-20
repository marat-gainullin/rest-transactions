package com.bearsoft.transactions.integration;

import com.bearsoft.transactions.services.TransactionsRepository;
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
    public TransactionsRepository transactions() {
        return new TransactionsRepository();
    }
}
