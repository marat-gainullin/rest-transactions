/**
 *
 */
package com.bearsoft.transactions.exceptions;

/**
 * This exception is thrown by main logic, when a transaction with the same id as in added trnasaction already
 * exists in the repository.
 * @author mg
 */
public class TransactionAlreadyExistsException extends RuntimeException {

    /**
     * A transaction key
     */
    private final long id;

    public TransactionAlreadyExistsException(final long aId) {
        super();
        id = aId;
    }

    public long getId() {
        return id;
    }

    @Override
    public final String getMessage() {
        return String.format("Transaction with id %d already exists", id);
    }

}
