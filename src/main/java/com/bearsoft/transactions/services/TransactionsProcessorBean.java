package com.bearsoft.transactions.services;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.model.TransactionsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * <code>TransactionsProcessor</code> default implementation.
 *
 * @author mg
 * @see TransactionsProcessor
 */
@Service
@Scope("prototype")
public class TransactionsProcessorBean implements TransactionsProcessor {

    /**
     * Collects ids of transactions of a <code>aType</code> type. Provides a
     * client code with an empty list if none transactions matched.
     *
     * @param aStore <code>TransactionsStore</code> to process.
     * @param aType A transactions type.
     * @return A collection of ids of the transactions with <code>aType</code>
     * type.
     */
    @Override
    public final Collection<Long> collectIds(final TransactionsStore aStore,
            final String aType) {
        Collection<Transaction> typesTransactions = aStore.get(aType);
        if (typesTransactions != null) {
            return typesTransactions.stream().map((transaction) -> {
                return transaction.getId();
            }).collect(Collectors.toCollection(() -> {
                return new ArrayList<>();
            }));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Gets a deep sum of transactions' amounts.
     *
     * @param aStore aStore <code>TransactionsStore</code> to process.
     * @param aId A root transaction id.
     * @return A deep sum amount.
     * @throws TransactionNotFoundException If no transaction with
     * <code>aId</code> found in the <code>aStore</code> storage.
     * @throws TransactionInCycleException If a cycle in transaction subtree
     * detected.
     */
    @Override
    public final double deepSum(final TransactionsStore aStore, final long aId)
            throws TransactionNotFoundException, TransactionInCycleException {
        Transaction parent = aStore.get(aId);
        if (parent != null) {
            return aStore.deepAmount(parent);
        } else {
            throw new TransactionNotFoundException(aId);
        }
    }
}
