package com.bearsoft.transactions.web;

/**
 *
 * @author mg
 */
public class TransactionResult {

    private final String status;

    public TransactionResult() {
        super();
        status = "ok";
    }

    public TransactionResult(String aValue) {
        status = aValue;
    }

    public final String getStatus() {
        return status;
    }
}
