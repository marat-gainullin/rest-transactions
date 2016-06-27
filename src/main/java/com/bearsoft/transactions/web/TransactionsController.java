package com.bearsoft.transactions.web;

import com.bearsoft.transactions.exceptions.TransactionAlreadyExistsException;
import com.bearsoft.transactions.exceptions.TransactionInCycleException;
import com.bearsoft.transactions.exceptions.TransactionNotFoundException;
import com.bearsoft.transactions.model.Transaction;
import com.bearsoft.transactions.model.TransactionsProcessor;
import com.bearsoft.transactions.model.TransactionsStore;
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
 * Rest endpoint implementation.
 *
 * @author mg
 */
@RestController
public class TransactionsController {

    /**
     * Transactions store instance.
     *
     * @see TransactionsStore
     */
    @Autowired
    private TransactionsStore transactions;
    /**
     * Transactions processor instance.
     *
     * @see TransactionsProcessor
     */
    @Autowired
    private TransactionsProcessor processor;

    /**
     * Http method "put" handler.
     *
     * @param aTransactionId A new transaction key.
     * @param aTransactionBody A new transaction information.
     * @return <code>TransactionResult</code> instance with information about
     * @throws TransactionAlreadyExistsException
     * transaction status.
     * @see TransactionBody
     * @see TransactionResult
     */
    @RequestMapping(value = "transaction/{transaction-id}",
            method = RequestMethod.PUT,
            consumes = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_PLAIN_VALUE},
            produces = {
                MediaType.APPLICATION_JSON_VALUE})
    public final TransactionResult put(
            @PathVariable("transaction-id") final long aTransactionId,
            @RequestBody final TransactionBody aTransactionBody)
            throws TransactionAlreadyExistsException {
        Transaction alreadyTransaction = transactions.putIfAbsent(
                aTransactionId,
                new Transaction(aTransactionId, aTransactionBody.getAmount(),
                        aTransactionBody.getType(),
                        aTransactionBody.getParent_id()));
        if (alreadyTransaction == null) {
            return new TransactionResult();
        } else {
            throw new TransactionAlreadyExistsException(aTransactionId);
        }
    }

    /**
     * Http method "get" handler. Retrieves information on a transaction by a
     * transaction's key.
     *
     * @param aTransactionId A key of atransaction to retrieve.
     * @return A transaction information in the <code>TransactionBody</code>
     * form.
     * @throws TransactionNotFoundException
     * @see TransactionBody
     */
    @RequestMapping(value = "transaction/{transaction-id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public final TransactionBody get(
            @PathVariable("transaction-id") final long aTransactionId)
            throws TransactionNotFoundException {
        Transaction transaction = transactions.get(aTransactionId);
        if (transaction != null) {
            return new TransactionBody(transaction);
        } else {
            throw new TransactionNotFoundException(aTransactionId);
        }
    }

    /**
     * Http method "get" handler. Retrieves a list of transactions by a
     * transaction's type.
     *
     * @param aTransactionType A transactions type list of wich is to be
     * returned.
     * @return A collection of transaction of the desired type.
     */
    @RequestMapping(value = "types/{transaction-type}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public final Collection<Long> get(
            @PathVariable("transaction-type") final String aTransactionType) {
        return processor.collectIds(transactions, aTransactionType);
    }

    /**
     * Http method "get" handler. Retrieves a sum of transaction amount across
     * transactions subtree including a root transaction.
     *
     * @param aTransactionId A root transaction key for amounts deep sum
     * calculation.
     * @return <code>SumResult</code> instance with information on calculated
     * sum.
     * @throws TransactionNotFoundException
     * @throws TransactionInCycleException
     * @see SumResult
     */
    @RequestMapping(value = "sum/{transaction-id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public final SumResult sum(
            @PathVariable("transaction-id") final long aTransactionId)
            throws TransactionNotFoundException, TransactionInCycleException {
        return new SumResult(processor.deepSum(transactions, aTransactionId));
    }

    /**
     * Handles <code>TransactionNotFoundException</code> exceptions.
     *
     * @param ex A <code>TransactionNotFoundException</code> instance.
     * @return A <code>TransactionResult</code> instance to be binded as
     * graceful http response.
     * @see TransactionNotFoundException
     * @see TransactionResult
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TransactionNotFoundException.class)
    public final TransactionResult handleNotFound(final Exception ex) {
        return new TransactionResult(ex.getMessage());
    }

    /**
     * Handles <code>TransactionAlreadyExistsException</code> and
     * <code>TransactionInCycleException</code> exceptions.
     *
     * @param ex A <code>TransactionAlreadyExistsException</code> or
     * <code>TransactionInCycleException</code> instance.
     * @return A <code>TransactionResult</code> instance to be binded as
     * graceful http response.
     *
     * @see TransactionAlreadyExistsException
     * @see TransactionInCycleException
     * @see TransactionResult
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
        TransactionAlreadyExistsException.class,
        TransactionInCycleException.class})
    public final TransactionResult handleAlreadyEsists(final Exception ex) {
        return new TransactionResult(ex.getMessage());
    }
}
