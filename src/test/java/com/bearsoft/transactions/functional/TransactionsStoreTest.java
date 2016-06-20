package com.bearsoft.transactions.functional;

import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsMemoryStore;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mg
 */
public class TransactionsStoreTest {

    private static final int TEST_TRANSACTIONS_COUNT = 50;
    private static final String TEST_TRANSACTIONS_TYPE = "fruits";

    @Test
    public void sameTypeTest() {
        TransactionsMemoryStore store = new TransactionsMemoryStore();
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            store.putIfAbsent(new Transaction(id, 5 * id, TEST_TRANSACTIONS_TYPE));
        }
        Collection<Transaction> byType = store.get(TEST_TRANSACTIONS_TYPE);
        assertEquals(TEST_TRANSACTIONS_COUNT, byType.size());// Since type is the same
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            assertEquals(id, store.get(id).getId());
            assertEquals(id * 5, (long) store.get(id).getAmount());// Since test amounts are integers in fact
            assertEquals((long) store.get(id).getAmount(), (long) store.deepAmount(store.get(id)));
        }
    }

    @Test
    public void variousTypesTest() {
        TransactionsMemoryStore store = new TransactionsMemoryStore();
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            store.putIfAbsent(new Transaction(id, 5 * id, TEST_TRANSACTIONS_TYPE + id));
        }
        for (long id = 0; id < TEST_TRANSACTIONS_COUNT; id++) {
            Collection<Transaction> byType = store.get(TEST_TRANSACTIONS_TYPE + id);
            assertEquals(1, byType.size());
            assertSame(byType.stream().findFirst().get(), store.get(id));
        }
    }

    @Test
    public void deepAmountForwardTest() {
        TransactionsMemoryStore store = new TransactionsMemoryStore();
        Transaction firstParent = new Transaction(0, 0, TEST_TRANSACTIONS_TYPE);
        store.putIfAbsent(firstParent);
        double expectedTotal = 0;
        for (long id = 1; id < TEST_TRANSACTIONS_COUNT; id++) {
            double amount = 5 * id;
            expectedTotal += amount;
            store.putIfAbsent(new Transaction(id, amount, TEST_TRANSACTIONS_TYPE, id - 1L));
        }
        double totalWithoutCachedChildren = store.deepAmount(firstParent);
        assertEquals((long) expectedTotal, (long) totalWithoutCachedChildren);// Since test amounts are integers in fact
        double totalWithCachedChildren = store.deepAmount(firstParent);
        assertEquals((long) expectedTotal, (long) totalWithCachedChildren);// Since test amounts are integers in fact
    }

    @Test
    public void deepAmountBackwardTest() {
        TransactionsMemoryStore store = new TransactionsMemoryStore();
        double expectedTotal = 0;
        for (long id = TEST_TRANSACTIONS_COUNT - 1; id > 0; id--) {
            double amount = 5 * id;
            expectedTotal += amount;
            store.putIfAbsent(new Transaction(id, amount, TEST_TRANSACTIONS_TYPE, id - 1L));
        }
        Transaction firstParent = new Transaction(0, 0, TEST_TRANSACTIONS_TYPE);
        store.putIfAbsent(firstParent);
        double totalWithoutCachedChildren = store.deepAmount(firstParent);
        assertEquals((long) expectedTotal, (long) totalWithoutCachedChildren);// Since test amounts are integers in fact
        double totalWithCachedChildren = store.deepAmount(firstParent);
        assertEquals((long) expectedTotal, (long) totalWithCachedChildren);// Since test amounts are integers in fact
    }
}
