package com.bearsoft.transactions.exceptions;

/**
 * This exception is thrown when cycle in transactions tree is detected.
 *
 * TODO: Add Cycle printing.
 *
 * @author mg
 */
public class TransactionInCycleException extends Exception {

    /**
     * Constructs the exception with a transaction key.
     *
     * @param aId A transaction id wich is in a cycle.
     */
    public TransactionInCycleException(final long aId) {
        super(String
                .format("Cycle in transactions tree with "
                        + "transaction id %d detected", aId));
    }

}
