package com.bearsoft.transactions.exceptions;

/**
 *
 * @author mg
 */
public class TransactionNotFoundException extends RuntimeException {

    protected long id;

    public TransactionNotFoundException(long aId) {
        super();
        id = aId;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getMessage() {
        return String.format("Transaction with id %d is not found", id);
    }

}
