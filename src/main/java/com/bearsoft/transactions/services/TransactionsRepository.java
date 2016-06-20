package com.bearsoft.transactions.services;

import com.bearsoft.transactions.model.TransactionsMemoryStore;
import org.springframework.stereotype.Repository;

/**
 * Separates Spring from Transactions class logic.
 * If there will be another storage types i.e. database, files etc,
 * than Transactions class should be transformed into iterface and multiple implementations
 * should be created.
 * @author mg
 */
@Repository
public class TransactionsRepository extends TransactionsMemoryStore {

    public TransactionsRepository() {
        super();
    }

}
