package com.example.seerbitjb.transaction;

public interface TransactionService {
    void createTransaction(TransactionRequestDto transactionRequestDto);
    StatisticsDetailsDto getStatistics();
    void deleteTransactions();
}
