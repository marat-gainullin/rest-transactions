package com.bearsoft.transactions.model;

import java.util.Collection;

/**
 * This class is pure model and it can be moved to anoter environment without
 * changes. It is immuable because of nature of the task and to avoid multicore
 * issues.
 *
 * @author mg
 */
public class Transaction {

    /**
     * A transaction's key.
     */
    private final long id;
    /**
     * A transaction's amount.
     */
    private final double amount;
    /**
     * A transaction's type.
     */
    private final String type;
    /**
     * A transaction's parent key.
     */
    private final Long parentId;
    /**
     * A transaction's cached children collection.
     */
    private volatile Collection<Transaction> children;

    /**
     * Constructs root transaction.
     *
     * @param aId A transaction key.
     * @param aAmount A transaction amount.
     * @param aType A transaction type.
     */
    public Transaction(final long aId,
            final double aAmount, final String aType) {
        this(aId, aAmount, aType, null);
    }

    /**
     * Constructs child transaction.
     *
     * @param aId A transaction key.
     * @param aAmount A transaction amount.
     * @param aType A transaction type.
     * @param aParentId A parent transaction's key.
     */
    public Transaction(final long aId, final double aAmount,
            final String aType, final Long aParentId) {
        super();
        id = aId;
        amount = aAmount;
        type = aType;
        parentId = aParentId;
    }

    /**
     * Transaction's key getter.
     *
     * @return The transaction's key.
     */
    public final long getId() {
        return id;
    }

    /**
     * Transaction parent's key getter.
     *
     * @return The transaction parent's key.
     */
    public final Long getParentId() {
        return parentId;
    }

    /**
     * Transaction's amount getter.
     *
     * @return The transaction's amount.
     */
    public final double getAmount() {
        return amount;
    }

    /**
     * Transaction's type getter.
     *
     * @return The transaction's type.
     */
    public final String getType() {
        return type;
    }

    /**
     * Transaction's cached children collection setter.
     *
     * @param aChildren A children collection that will be cached for further
     * use.
     */
    public final void setChildren(final Collection<Transaction> aChildren) {
        children = aChildren;
    }

    /**
     * Transaction's cached children collection getter.
     *
     * @return A cached children collection.
     */
    public final Collection<Transaction> getChildren() {
        return children;
    }

}
