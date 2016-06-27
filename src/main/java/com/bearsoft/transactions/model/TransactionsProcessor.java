package com.bearsoft.transactions.model;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import java.util.Collection;

/**
 * Transactions processor interface. Implementatinos of this interface should
 * process <code>TransactionsStore</code> instances and contain logic that is
 * separated from particular <code>TransactionsStore</code> implementation.
 *
 * @see TransactionsStore
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
     * @throws TransactionNotFoundException
     * @throws TransactionInCycleException
     */
    double deepSum(TransactionsStore aStore, long aId)
            throws TransactionNotFoundException, TransactionInCycleException;
}
