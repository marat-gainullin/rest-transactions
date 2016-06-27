package com.bearsoft.transactions.services;

import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.model.TransactionsStore;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * This class is a Spring service.
 * Its logic can be moved to another environment without changes.
 *
 * @author mg
 */
@Service
@Scope("prototype")
public class TransactionsProcessorBean implements TransactionsProcessor {

    /**
     * Collects ids of transactions of a <code>aType</code> type.
     * Provides a client code with an empty list if none transactions
     * matched.
     * @param aStore <code>TransactionsStore</code> to process.
     * @param aType A transactions type.
     * @return A collection of ids of the transactions with <code>aType</code> type.
     */
    @Override
    public Collection<Long> collectIds(TransactionsStore aStore, String aType) {
        Collection<Transaction> typesTransactions = aStore.get(aType);
        if (typesTransactions != null) {
            List<Long> ids = new LinkedList<>();
            typesTransactions.stream().forEach((transaction) -> {
                ids.add(transaction.getId());
            });
            return ids;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Calculates a deep sum of transactions' amounts.
     * @param aStore aStore <code>TransactionsStore</code> to process.
     * @param aId A root transaction id.
     * @return A deep sum amount.
     */
    @Override
    public double deepSum(TransactionsStore aStore, long aId) {
        Transaction parent = aStore.get(aId);
        if (parent != null) {
            return aStore.deepAmount(parent);
        } else {
            throw new TransactionNotFoundException(aId);
        }
    }
}
