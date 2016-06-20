package com.bearsoft.transactions.web;

import com.bearsoft.transactions.exceptions.TransactionAlreadyExistsException;
import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.services.TransactionsRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mg
 */
@RestController
public class TransactionsController {

    /**
     * Inner class because of "sum" property. It should be something like
     * "OperationResult" and it should be a simple ordinary public class, but
     * "sum" property can be used only for sum and so, i want to put it here as
     * inner class.
     */
    public static class SumResult {

        private final double sum;

        public SumResult(final double aValue) {
            sum = aValue;
        }

        public final double getSum() {
            return sum;
        }

    }

    @Autowired
    /**
     * may be the lifecycle will be changed by means of the task...
     */
    private TransactionsRepository transactions;

    @RequestMapping(value = "transaction/{transaction-id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public final TransactionResult put(@PathVariable("transaction-id") final long aTransactionId, @RequestBody final TransactionBody aTransactionBody) {
        Transaction alreadyTransaction = transactions.putIfAbsent(new Transaction(aTransactionId, aTransactionBody.getAmount(), aTransactionBody.getType(), aTransactionBody.getParent_id()));
        if (alreadyTransaction == null) {
            return new TransactionResult();
        } else {
            throw new TransactionAlreadyExistsException(aTransactionId);
        }
    }

    @RequestMapping(value = "transaction/{transaction-id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public final TransactionBody get(@PathVariable("transaction-id") final long aTransactionId) {
        Transaction transaction = transactions.get(aTransactionId);
        if (transaction != null) {
            return new TransactionBody(transaction);
        } else {
            throw new TransactionNotFoundException(aTransactionId);
        }
    }

    @RequestMapping(value = "types/{transaction-type}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public final Collection<Long> get(@PathVariable("transaction-type") final String aTransactionType) {
        return TransactionsProcessor.collectIds(transactions, aTransactionType);
    }

    @RequestMapping(value = "sum/{transaction-id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public final SumResult sum(@PathVariable("transaction-id") final long aTransactionId) {
        return new SumResult(TransactionsProcessor.deepSum(transactions, aTransactionId));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TransactionNotFoundException.class)
    public final TransactionResult handleNotFound(final Exception ex) {
        return new TransactionResult(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({TransactionAlreadyExistsException.class, TransactionInCycleException.class})
    public final TransactionResult handleAlreadyEsists(final Exception ex) {
        return new TransactionResult(ex.getMessage());
    }
}
