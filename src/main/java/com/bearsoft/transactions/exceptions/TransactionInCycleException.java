package com.bearsoft.transactions.exceptions;

import com.bearsoft.transactions.model.Transaction;

/**
 *
 * @author mg
 */
public class TransactionInCycleException extends RuntimeException {

    public TransactionInCycleException(Transaction aTransaction) {
        // TODO: add Cycle printing
        super(String.format("Cycle in transaction tree with transaction id %d detected", aTransaction.getId()));
    }

}
