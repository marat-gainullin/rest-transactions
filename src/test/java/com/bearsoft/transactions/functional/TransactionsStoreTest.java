package com.bearsoft.transactions.functional;

import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.services.TransactionsMemoryStoreBean;
import com.bearsoft.transactions.model.TransactionsStore;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This id test suite of <code>TransactionsStore</code>
 * <code>TransactionsMemoryStoreBean</code> implementation.
 *
 * @author mg
 */
public class TransactionsStoreTest {

    /**
     * Test samples count.
     */
    private static final int TEST_TRANSACTIONS_COUNT = 50;
    /**
     * Transactions type to be used across thw whole test suite.
     */
    private static final String TEST_TRANSACTIONS_TYPE = "fruits";

    /**
     * Checks get by type correctness for several transaction of the same type.
     *
     * @throws TransactionInCycleException
     */
    @Test
    public void sameTypeTest() throws TransactionInCycleException {
        TransactionsStore store = new TransactionsMemoryStoreBean();
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            store.putIfAbsent(id,
                    new Transaction(id, 5 * id, TEST_TRANSACTIONS_TYPE));
        }
        Collection<Transaction> byType = store.get(TEST_TRANSACTIONS_TYPE);
        //Since type is the same
        assertEquals(TEST_TRANSACTIONS_COUNT, byType.size());
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            assertEquals(id, store.get(id).getId());
            // Since test amounts are integers in fact
            assertEquals(id * 5, (long) store.get(id).getAmount());
            assertEquals(
                    (long) store.get(id).getAmount(),
                    (long) store.deepAmount(store.get(id)));
        }
    }

    /**
     * Checks get by type correctness for several transaction of the different
     * types.
     */
    @Test
    public void variousTypesTest() {
        TransactionsStore store = new TransactionsMemoryStoreBean();
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            store.putIfAbsent(id,
                    new Transaction(id, 5 * id, TEST_TRANSACTIONS_TYPE + id));
        }
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            Collection<Transaction> byType = store
                    .get(TEST_TRANSACTIONS_TYPE + id);
            assertEquals(1, byType.size());
            assertSame(byType.stream().findFirst().get(), store.get(id));
        }
    }

    /**
     * Checks correctness of deep sum operation for transactions tree that is
     * constructed from root to leaves.
     *
     * @throws TransactionInCycleException
     */
    @Test
    public void deepAmountForwardTest() throws TransactionInCycleException {
        TransactionsStore store = new TransactionsMemoryStoreBean();
        Transaction firstParent = new Transaction(0, 0, TEST_TRANSACTIONS_TYPE);
        store.putIfAbsent(0, firstParent);
        double expectedTotal = 0;
        for (long id = 1; id < TEST_TRANSACTIONS_COUNT; id++) {
            double amount = 5 * id;
            expectedTotal += amount;
            store.putIfAbsent(id,
                    new Transaction(id, amount,
                            TEST_TRANSACTIONS_TYPE, id - 1L));
        }
        double totalWithoutCachedChildren = store.deepAmount(firstParent);
        // Since test amounts are integers in fact
        assertEquals((long) expectedTotal, (long) totalWithoutCachedChildren);
        double totalWithCachedChildren = store.deepAmount(firstParent);
        // Since test amounts are integers in fact
        assertEquals((long) expectedTotal, (long) totalWithCachedChildren);
    }

    /**
     * Checks correctness of deep sum operation for transactions tree that is
     * constructed from leaves to root.
     *
     * @throws TransactionInCycleException
     */
    @Test
    public void deepAmountBackwardTest() throws TransactionInCycleException {
        TransactionsStore store = new TransactionsMemoryStoreBean();
        double expectedTotal = 0;
        for (long id = TEST_TRANSACTIONS_COUNT - 1; id > 0; id--) {
            double amount = 5 * id;
            expectedTotal += amount;
            store.putIfAbsent(id,
                    new Transaction(id, amount,
                            TEST_TRANSACTIONS_TYPE, id - 1L));
        }
        Transaction firstParent = new Transaction(0, 0, TEST_TRANSACTIONS_TYPE);
        store.putIfAbsent(0, firstParent);
        double totalWithoutCachedChildren = store.deepAmount(firstParent);
        // Since test amounts are integers in fact
        assertEquals((long) expectedTotal, (long) totalWithoutCachedChildren);
        double totalWithCachedChildren = store.deepAmount(firstParent);
        // Since test amounts are integers in fact
        assertEquals((long) expectedTotal, (long) totalWithCachedChildren);
    }
}
