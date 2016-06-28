package com.bearsoft.transactions.exceptions;

/**
 * This exception is thrown by main logic, when a transaction with the same id
 * as in added transaction already exists in the repository.
 *
 * @author mg
 */
public class TransactionAlreadyExistsException extends Exception {

    /**
     * Construct the exception with specific transaction key.
     * @param aId A key of the transaction.
     */
    public TransactionAlreadyExistsException(final long aId) {
        super(String.format("Transaction with id %d already exists", aId));
    }

}
