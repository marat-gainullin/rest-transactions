package com.bearsoft.transactions.model;

import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class may be Spring service, but it is static and therefore stateless
 * and its logic can be moved to another environment without changes.
 *
 * @author mg
 */
public class TransactionsProcessor {

    public static Collection<Long> collectIds(TransactionsMemoryStore aStore, String aType) {
        Collection<Transaction> typesTransactions = aStore.get(aType);
        if (typesTransactions != null) {
            List<Long> ids = new LinkedList<>();
            typesTransactions.stream().forEach((transaction) -> {
                ids.add(transaction.getId());
            });
            return ids;
        } else {
            return new LinkedList<>();// provide client code with an empty list
        }
    }

    public static double deepSum(TransactionsMemoryStore aStore, long aId) {
        Transaction parent = aStore.get(aId);
        if (parent != null) {
            return aStore.deepAmount(parent);
        } else {
            throw new TransactionNotFoundException(aId);
        }
    }
}
