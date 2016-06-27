package com.bearsoft.transactions.model;

import java.util.Collection;

/**
 * This class is a Spring service. Its logic can be moved to another environment
 * without changes.
 *
 * @author mg
 */
public interface TransactionsProcessor {

    /**
     * Collects ids of transactions of a <code>aType</code> type. Provides a
     * client code with an empty list if none transactions matched.
     *
     * @param aStore <code>TransactionsStore</code> to process.
     * @param aType A transactions type.
     * @return A collection of ids of the transactions with <code>aType</code>
     * type.
     */
    Collection<Long> collectIds(TransactionsStore aStore, String aType);

    /**
     * Calculates a deep sum of transactions' amounts.
     *
     * @param aStore aStore <code>TransactionsStore</code> to process.
     * @param aId A root transaction id.
     * @return A deep sum amount.
     */
    double deepSum(TransactionsStore aStore, long aId);
}
