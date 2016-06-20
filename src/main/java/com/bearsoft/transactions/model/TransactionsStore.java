package com.bearsoft.transactions.model;

import java.util.Collection;

/**
 *
 * @author mg
 */
public interface TransactionsStore {

    /**
     * 
     * @param aParent
     * @return 
     */
    double deepAmount(Transaction aParent);

    /**
     * 
     * @param aId
     * @return 
     */
    Transaction get(long aId);

    /**
     * 
     * @param aType
     * @return 
     */
    Collection<Transaction> get(String aType);

    /**
     * 
     * @param aTransaction
     * @return 
     */
    Transaction putIfAbsent(Transaction aTransaction);

}
