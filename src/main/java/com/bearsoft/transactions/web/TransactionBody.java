package com.bearsoft.transactions.web;

import com.bearsoft.transactions.model.Transaction;

/**
 * This class is intended to be a transaction information container.
 *
 * @author mg
 */
public class TransactionBody {

    /**
     *
     */
    private Long parent_id;
    /**
     *
     */
    private double amount;
    /**
     *
     */
    private String type;

    /**
     * Creates an instanceof TransactionBody with information copied from a
     * Transaction instance.
     *
     * @param aTransaction
     */
    public TransactionBody(final Transaction aTransaction) {
        super();
        parent_id = aTransaction.getParentId();
        amount = aTransaction.getAmount();
        type = aTransaction.getType();
    }

    /**
     * Creates an empty TransactionBody.
     */
    public TransactionBody() {
        super();
    }

    /**
     * Returns amount of the transaction.
     *
     * @return Amount from this transaction.
     */
    public final double getAmount() {
        return amount;
    }

    /**
     * Sets an amount.
     *
     * @param aValue Value of amount.
     */
    public final void setAmount(final double aValue) {
        amount = aValue;
    }

    /**
     * Returns type of a transaction.
     *
     * @return Type of a transaction.
     */
    public final String getType() {
        return type;
    }

    /**
     * Sets type of the transaction.
     *
     * @param aValue Type of the transaction.
     */
    public final void setType(final String aValue) {
        type = aValue;
    }

    /**
     * Returns parent_id of the transaction.
     *
     * @return parent_id of the transaction.
     */
    public final Long getParent_id() {
        return parent_id;
    }

    /**
     * Sets parent_id value from this transaction
     *
     * @param aValue parent_id value to setted.
     */
    public final void setParent_id(final Long aValue) {
        parent_id = aValue;
    }

}
