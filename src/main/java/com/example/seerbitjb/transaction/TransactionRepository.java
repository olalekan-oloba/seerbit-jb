package com.example.seerbitjb.transaction;

import jakarta.annotation.Nullable;

import java.util.Collection;

public interface TransactionRepository {
    void save(Transaction transaction);
    @Nullable
    Collection<Transaction> getTransactions();//TODO: remove unnecessay method
    Statistics getStatistics();
    void deleteTransactions();
}
