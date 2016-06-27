package com.bearsoft.transactions.functional;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.services.TransactionsMemoryStoreBean;
import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.services.TransactionsProcessorBean;
import com.bearsoft.transactions.model.TransactionsStore;
import org.junit.Test;

/**
 * Test suite of <code>TransactionProcessor</code>.
 *
 * @author mg
 */
public class TransactionProcessorTest {

    /**
     * This test case ensures, that <code>TransactionNotFoundException</code> is
     * thrown.
     *
     * @throws TransactionNotFoundException
     * @throws TransactionInCycleException
     * @see TransactionNotFoundException
     */
    @Test(expected = TransactionNotFoundException.class)
    public void notFoundTest()
            throws TransactionNotFoundException, TransactionInCycleException {
        TransactionsProcessor processor = new TransactionsProcessorBean();
        TransactionsStore store = new TransactionsMemoryStoreBean();
        processor.deepSum(store, 1L);
    }

}
