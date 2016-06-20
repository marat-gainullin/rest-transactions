package com.bearsoft.transactions.model;

import java.util.Collection;
import java.util.Comparator;

/**
 * This class is pure model and it can be moved to anoter environment without
 * changes. It is immuable because of nature of the task and to avoid multicore
 * issues.
 *
 * @author mg
 */
public class Transaction {

    private final long id;
    private final double amount;
    private final String type;
    private final Long parentId;
    private volatile Collection<Transaction> children;

    public Transaction(long aId, double aAmount, String aType) {
        this(aId, aAmount, aType, null);
    }

    public Transaction(long aId, double aAmount, String aType, Long aParentId) {
        super();
        id = aId;
        amount = aAmount;
        type = aType;
        parentId = aParentId;
    }

    public final long getId() {
        return id;
    }

    public final Long getParentId() {
        return parentId;
    }

    public final double getAmount() {
        return amount;
    }

    public final String getType() {
        return type;
    }

    /**
     * Pakage private to avoid modifications by client code
     *
     * @param aChildren
     */
    void setChildren(Collection<Transaction> aChildren) {
        children = aChildren;
    }

    /**
     * Package private to avoid modifications by client code
     *
     * @return raw children collection.
     */
    final Collection<Transaction> getChildren() {
        return children;
    }

    public static Comparator<Transaction> comparator() {
        return (Transaction o1, Transaction o2) -> {
            if (o1.id > o2.id) {
                return 1;
            } else if (o1.id < o2.id) {
                return -1;
            } else {
                return 0;
            }
        };
    }
}
