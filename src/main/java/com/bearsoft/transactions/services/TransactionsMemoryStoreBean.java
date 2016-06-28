package com.bearsoft.transactions.services;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsStore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
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
    public final <U> U reduce(final long aRootId, final U aIdentity,
            @NotNull final BiFunction<Transaction, U, U> aHandler)
            throws TransactionInCycleException, TransactionNotFoundException {
        Transaction aParent = transactionsById.get(aRootId);
        if (aParent != null) {
            return walk(aParent, aIdentity, aHandler, new HashSet<>());
        } else {
            throw new TransactionNotFoundException(aRootId);
        }
    }

    /**
     * Walks through a transaction subtree and reduces transaction.
     *
     * @param aParent The root transaction of processed subtree.
     * @param aMetTransactions A set of met transaction for cycles detection.
     * @return A reduced value of transactions subtree.
     * @throws TransactionInCycleException
     */
    private <U> U walk(@NotNull final Transaction aParent, final U aIdentity, 
            final BiFunction<Transaction, U, U> aHandler,
            final Set<Long> aMetTransactions)
            throws TransactionInCycleException {
        // It seems it is race condition.
        // We rely here on store conract, wich says, that
        // this method on a particular transaction id is called
        // only after ther trasnaction is added, e.g. within a callback of
        // response on transaction addition request.
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
            U result = aHandler.apply(aParent, aIdentity);
            if (children != null) {
                for (Transaction aChild : children) {
                    result = walk(aChild, result, aHandler, aMetTransactions);
                }
            }
            return result;
        } else {
            throw new TransactionInCycleException(aParent.getId());
        }
    }
}
