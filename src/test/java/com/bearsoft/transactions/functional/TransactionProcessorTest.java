package com.bearsoft.transactions.functional;

import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.services.TransactionsMemoryStoreBean;
import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.services.TransactionsProcessorBean;
import com.bearsoft.transactions.model.TransactionsStore;
import org.junit.Test;

/**
 *
 * @author mg
 */
public class TransactionProcessorTest {


    @Test(expected = TransactionNotFoundException.class)
    public void notFoundTest() {
        TransactionsProcessor processor = new TransactionsProcessorBean();
        TransactionsStore store = new TransactionsMemoryStoreBean();
        processor.deepSum(store, 1L);
    }

}
