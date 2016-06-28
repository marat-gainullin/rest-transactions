package com.bearsoft.transactions.web;

import com.bearsoft.transactions.model.Transaction;

/**
 * This class is intended to be a transaction information container. Requests
 * should contain fields like a 'parent_id', but we do not want to include such
 * fields in <code>Transaction</code> class.
 *
 * @see Transaction
 * @author mg
 */
public class TransactionBody {

    /**
     * A transaction's parent key. Property name is 'parent_id' according to the
     * task :(.
     */
    private Long parent_id;
    /**
     * A transaction's amount.
     */
    private double amount;
    /**
     * A transaction's type.
     */
    private String type;

    /**
     * Creates an instance of <code>TransactionBody</code> with information
     * copied from a <code>Transaction</code> instance.
     *
     * @param aTransaction A <code>Transaction</code> instance the wrapper is
     * created for.
     * @see Transaction
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
     * Transaction's amount getter.
     *
     * @return Amount from this transaction.
     */
    public final double getAmount() {
        return amount;
    }

    /**
     * Transaction's amount setter.
     *
     * @param aValue Value of amount.
     */
    public final void setAmount(final double aValue) {
        amount = aValue;
    }

    /**
     * Transaction's type getter.
     *
     * @return Type of a transaction.
     */
    public final String getType() {
        return type;
    }

    /**
     * Transaction's type setter.
     *
     * @param aValue Type of the transaction.
     */
    public final void setType(final String aValue) {
        type = aValue;
    }

    /**
     * Transaction's parent_id getter. Method name is 'getParent_id' according
     * to the task :(.
     *
     * @return parent_id of the transaction.
     */
    public final Long getParent_id() {
        return parent_id;
    }

    /**
     * Transaction's parent_id setter. Method name is 'setParent_id' according
     * to the task :(.
     *
     * @param aValue parent_id value to setted.
     */
    public final void setParent_id(final Long aValue) {
        parent_id = aValue;
    }

}
