package com.bearsoft.transactions.services;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsStore;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

/**
 * This class is pure storage and it can be moved to anoter environment without
 * changes.
 *
 * @author mg
 */
@Repository
public class TransactionsMemoryStoreBean implements TransactionsStore {

    private final Map<Long, Transaction> transactionsById = new ConcurrentSkipListMap<>();// log(n) of get() and put()
    private final Map<String, Collection<Transaction>> transactionsByType = new ConcurrentSkipListMap<>();// log(n) of get() and put()
    private final Map<Long, Collection<Transaction>> transactionsByParent = new ConcurrentSkipListMap<>();// log(n) of get() and put()

    @Override
    public Transaction putIfAbsent(Transaction aTransaction) {
        Transaction alreadyTransaction = transactionsById.putIfAbsent(aTransaction.getId(), aTransaction);
        if (alreadyTransaction == null) {
            // It is guaranteed here, that transactions in the transactionsById map are unique
            Collection<Transaction> typedTransactions = new ConcurrentSkipListSet<>(Transaction.comparator());
            typedTransactions.add(aTransaction);// To avoid visibility of empty collection while 'deepAmount'
            // putIfAbsent and alreadyTypedTransactions.add() guarantee identity of transactions collection of the same type
            Collection<Transaction> alreadyTypedTransactions = transactionsByType.putIfAbsent(aTransaction.getType(), typedTransactions);
            if (alreadyTypedTransactions != null) {
                alreadyTypedTransactions.add(aTransaction);
            }
            if (aTransaction.getParentId() != null) {
                Collection<Transaction> parentedTransactions = new ConcurrentSkipListSet<>(Transaction.comparator());
                parentedTransactions.add(aTransaction);// To avoid visibility of empty collection while 'deepAmount'
                // putIfAbsent and alreadyParentedTransactions.add() guarantee identity of children collection
                Collection<Transaction> alreadyParentedTransactions = transactionsByParent.putIfAbsent(aTransaction.getParentId(), parentedTransactions);
                if (alreadyParentedTransactions != null) {
                    alreadyParentedTransactions.add(aTransaction);
                }
            }
            return null;
        } else {
            return alreadyTransaction;
        }
    }

    @Override
    public Transaction get(long aId) {
        return transactionsById.get(aId);
    }

    @Override
    public Collection<Transaction> get(String aType) {
        Collection<Transaction> byType = transactionsByType.get(aType);
        return byType != null ? Collections.unmodifiableCollection(byType) : null;
    }

    @Override
    public double deepAmount(Transaction aParent) {
        return deepAmount(aParent, new TreeSet<>(Transaction.comparator()));
    }

    public double deepAmount(@NotNull Transaction aParent, Set<Transaction> aMetTransactions) {
        if (!aMetTransactions.contains(aParent)) {
            aMetTransactions.add(aParent);
            // put() provides us with an identity for children collection guarantee 
            Collection<Transaction> children = aParent.getChildren();
            if (children == null) {
                children = transactionsByParent.get(aParent.getId());
                if (children != null) {
                    aParent.setChildren(children);
                }
            }
            double sum = aParent.getAmount();
            if (children != null) {
                for (Transaction child : children) {
                    sum += deepAmount(child, aMetTransactions);
                }
            }
            return sum;
        } else {
            throw new TransactionInCycleException(aParent);
        }
    }
}
