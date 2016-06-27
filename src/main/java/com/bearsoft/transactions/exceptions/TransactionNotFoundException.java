package com.bearsoft.transactions.exceptions;

/**
 *
 * @author mg
 */
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(long aId) {
        super(String.format("Transaction with id %d is not found", aId));
    }

}
