package com.example.seerbitjb.transaction;

import java.util.Collection;

public interface TransactionRepository {
    void save(Transaction transaction);

    Collection<Transaction> getTransactions();
}
