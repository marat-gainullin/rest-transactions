package com.bearsoft.transactions.functional;

import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.model.TransactionsMemoryStore;
import com.bearsoft.transactions.model.TransactionsProcessor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author mg
 */
public class TransactionProcessorTest {

    @Test
    public void notFoundTest() {
        TransactionsMemoryStore store = new TransactionsMemoryStore();
        try {
            TransactionsProcessor.deepSum(store, 1L);
            fail("TransactionNotFoundException expected");
        } catch (TransactionNotFoundException ex) {
            assertEquals(1L, ex.getId());
        }
    }

}
