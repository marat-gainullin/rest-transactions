# Rest transactions

This is a coding training project.

It is mostly about Spring MVC.

## Asymptotic estimates of transactions service operations

Let "m" be a number of elements in a transactions sub tree (linked by "parent_id")
and let "n" be a number of transactions in the whole storage.

Operations "put", "get(type)" and "get(transaction_id)" will be performed with "log(n)" complexity.

Operation "sum" will be performed with "m * log(m) * log(n)" complexity at the first time.
At the subsequent times it will be performed with "m * log(m)" complexity because of using of "Transaction.children" property.

If we don't take into account cycles structure defect in the tree of transactions, than complexity of "sum" operation can be reduced to "m * log(n)" for the first time and to "m" for subsequent times for the same sub tree of transactions.
