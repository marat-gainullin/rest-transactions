package com.bearsoft.transactions.model;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import java.util.Collection;

/**
 * Transactions store interface. Implementations of this interface should
 * provide access to transactions for read by id, type. Also they should provide
 * access for transactions adding.
 *
 * @author mg
 */
public interface TransactionsStore {

    /**
     * Calcuates amount of transactions subtree recursively.
     *
     * @param aParent A root transaction of processed subtree.
     * @return An amount value calculated as deep sum including
     * <code>aParent</code> transaction amount.
     * @throws TransactionInCycleException
     */
    double deepAmount(Transaction aParent) throws TransactionInCycleException;

    /**
     * Retrieves a transaction by key.
     *
     * @param aId A transaction's key.
     * @return <code>Transaction</code> instance.
     * @see Transaction
     */
    Transaction get(long aId);

    /**
     * Retrieves a collections of transactions of the same type.
     *
     * @param aType Type of transactions to retrieve.
     * @return A collection of transactions of <code>aType</code> type.
     */
    Collection<Transaction> get(String aType);

    /**
     * Puts a transaction into the store if a transaction with the same id is
     * absent.
     *
     * @param aId A key to put transaction intoo the store.
     * @param aTransaction A transaction to put into the storage.
     * @return A transacaction that already present in the storage. If none,
     * than the return value is null.
     */
    Transaction putIfAbsent(final long aId, Transaction aTransaction);

}
