package com.example.seerbitjb.transaction;

import org.springframework.stereotype.Service;

/**
 * Process transaction service functionalities
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService{
    TransactionRepository repository;
    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * create and persist user transantion request
     * @param transactionRequestDto user ttransaction request
     */
    @Override
    public void createTransaction(TransactionRequestDto transactionRequestDto) {
        var transactionModel=createTransactionModel(transactionRequestDto);
        this.repository.save(transactionModel);
    }


    /**
     * Create transaction model from request DTO
     * @param transactionRequestDto user input request
     * @return created model
     */
    private Transaction createTransactionModel(TransactionRequestDto transactionRequestDto) {
        var transactionModel=new Transaction();
        transactionModel.setAmount(transactionRequestDto.getAmount());
        transactionModel.setTimeStamp(transactionRequestDto.getTimeStamp());
        return transactionModel;
    }
}
