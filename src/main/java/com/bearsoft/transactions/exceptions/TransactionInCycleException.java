package com.bearsoft.transactions.exceptions;

import com.bearsoft.transactions.model.Transaction;

/**
 *
 * @author mg
 */
public class TransactionInCycleException extends RuntimeException {

    private final Transaction transaction;

    public TransactionInCycleException(Transaction aTransaction) {
        super();
        transaction = aTransaction;
    }

    @Override
    public String getMessage() {
        // TODO: add Cycle printing
        return String.format("Cycle in transaction tree with transaction id %d detected", transaction.getId());
    }

}
