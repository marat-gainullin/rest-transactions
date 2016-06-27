package com.bearsoft.transactions.exceptions;

/**
 * Exception thrown when a transaction is not found.
 * @author mg
 */
public class TransactionNotFoundException extends Exception {

    /**
     * The exception constructor.
     * @param aId A transaction's key that was not found.
     */
    public TransactionNotFoundException(final long aId) {
        super(String.format("Transaction with id %d is not found", aId));
    }

}
