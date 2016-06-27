package com.bearsoft.transactions.services;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsStore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

/**
 * This class is memory storage ..
 *
 * @author mg
 */
@Repository
public class TransactionsMemoryStoreBean implements TransactionsStore {

    /**
     * log(n) of get() and put().
     */
    private final Map<Long, Transaction> transactionsById
            = new ConcurrentHashMap<>();
    /**
     * log(n) of get() and put().
     */
    private final Map<String, Collection<Transaction>> transactionsByType
            = new ConcurrentHashMap<>();
    /**
     * log(n) of get() and put().
     */
    private final Map<Long, Collection<Transaction>> transactionsByParent
            = new ConcurrentHashMap<>();

    @Override
    public final Transaction putIfAbsent(final long aId,
            final Transaction aTransaction) {
        Transaction alreadyTransaction = transactionsById.
                putIfAbsent(aId, aTransaction);
        if (alreadyTransaction == null) {
            // It is guaranteed here, that transactions in the transactionsById
            // map are unique.
            Collection<Transaction> typedTransactions = ConcurrentHashMap
                    .newKeySet();
            // To avoid visibility of empty collection while 'deepAmount'
            typedTransactions.add(aTransaction);
            // putIfAbsent and alreadyTypedTransactions.add() guarantee identity
            // of transactions collection of the same type
            Collection<Transaction> alreadyTypedTransactions
                    = transactionsByType.putIfAbsent(aTransaction.getType(),
                            typedTransactions);
            if (alreadyTypedTransactions != null) {
                alreadyTypedTransactions.add(aTransaction);
            }
            if (aTransaction.getParentId() != null) {
                Collection<Transaction> parentedTransactions = ConcurrentHashMap
                        .newKeySet();
                // To avoid visibility of empty collection while 'deepAmount'
                parentedTransactions.add(aTransaction);
                // putIfAbsent and alreadyParentedTransactions.add()
                // guarantee identity of children collection
                Collection<Transaction> alreadyParentedTransactions
                        = transactionsByParent.putIfAbsent(
                                aTransaction.getParentId(),
                                parentedTransactions);
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
    public final Transaction get(final long aId) {
        return transactionsById.get(aId);
    }

    @Override
    public final Collection<Transaction> get(final String aType) {
        Collection<Transaction> byType = transactionsByType.get(aType);
        return byType != null
                ? Collections.unmodifiableCollection(byType)
                : null;
    }

    @Override
    public final double deepAmount(final Transaction aParent)
            throws TransactionInCycleException {
        return deepAmount(aParent, new HashSet<>());
    }

    /**
     * Calculates amouts sum recursively across transactions subtree with
     * <code>aParent</code> root transaction including root transaction's
     * amount.
     *
     * @param aParent The root transaction of processed subtree.
     * @param aMetTransactions A set of met transaction for cycles detection.
     * @return A amounts sum of transactions subtree.
     * @throws TransactionInCycleException
     */
    private double deepAmount(@NotNull final Transaction aParent,
            final Set<Long> aMetTransactions)
            throws TransactionInCycleException {
        if (!aMetTransactions.contains(aParent.getId())) {
            aMetTransactions.add(aParent.getId());
            // put() provides us with an identity for
            // children collection guarantee.
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
            throw new TransactionInCycleException(aParent.getId());
        }
    }
}
