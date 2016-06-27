package com.bearsoft.transactions.web;

/**
 * Transaction result class. Responses should be formatted as {status: "ok"}.
 * So, this class is status property holder.
 *
 * @author mg
 */
public class TransactionResult {

    /**
     * Status property value.
     */
    private final String status;

    /**
     * "ok" status constructor.
     */
    public TransactionResult() {
        super();
        status = "ok";
    }

    /**
     * A some status consructor.
     *
     * @param aValue A status property value.
     */
    public TransactionResult(final String aValue) {
        status = aValue;
    }

    /**
     * Status property getter.
     *
     * @return Status property value.
     */
    public final String getStatus() {
        return status;
    }
}
