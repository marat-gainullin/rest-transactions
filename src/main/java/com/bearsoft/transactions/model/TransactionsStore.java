package com.bearsoft.transactions.model;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import java.util.Collection;
import java.util.function.BiFunction;
import javax.validation.constraints.NotNull;

/**
 * Transactions store interface. Implementations of this interface should
 * provide access to transactions for read by id, type. Also they should provide
 * access for transactions adding.
 *
 * @author mg
 */
public interface TransactionsStore {

    /**
     * Traverses transactions subtree recursively.
     *
     * @param <U> Type of result of the operation.
     * @param aRootId A root transaction key.
     * @param aIdentity The start value of the operation.
     * @param aAccumulator Accumulator function.
     * @return Result of the operation.
     * @throws TransactionInCycleException
     * @throws TransactionNotFoundException
     */
    <U> U reduce(final long aRootId, U aIdentity,
            @NotNull BiFunction<Transaction, U, U> aAccumulator)
            throws TransactionInCycleException, TransactionNotFoundException;

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
    Transaction putIfAbsent(long aId, Transaction aTransaction);

}
