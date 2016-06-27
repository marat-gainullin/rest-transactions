/**
 *
 */
package com.bearsoft.transactions.exceptions;

/**
 * This exception is thrown by main logic, when a transaction with the same id
 * as in added trnasaction already exists in the repository.
 *
 * @author mg
 */
public class TransactionAlreadyExistsException extends RuntimeException {

    public TransactionAlreadyExistsException(final long aId) {
        super(String.format("Transaction with id %d already exists", aId));
    }

}
