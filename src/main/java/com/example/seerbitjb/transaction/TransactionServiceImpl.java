package com.example.seerbitjb.transaction;

import org.springframework.stereotype.Service;

import java.math.RoundingMode;

import static com.example.seerbitjb.constant.AppConstants.BIGDECIMAL_SCALE;

/**
 * Process transaction service functionalities
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
    TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * create and persist user transantion request
     *
     * @param transactionRequestDto user ttransaction request
     */
    @Override
    public void createTransaction(TransactionRequestDto transactionRequestDto) {
        var transactionModel = createTransactionModel(transactionRequestDto);
        this.repository.save(transactionModel);
    }

    @Override
    public StatisticsDetailsDto getStatistics() {
        var statistic = this.repository.getStatistics();
        return mapStatisticModelToDto(statistic);
    }

    @Override
    public void deleteTransactions() {
       this.repository.deleteTransactions();
    }

    private StatisticsDetailsDto mapStatisticModelToDto(Statistics statistic) {
        //TODO: use a mapper class
        return StatisticsDetailsDto.builder()
                .avg(statistic.getAvg()==null?"0.00":statistic.getAvg().setScale(BIGDECIMAL_SCALE,RoundingMode.HALF_UP).toString())
                .count(String.valueOf(statistic.getCount()))
                .max(statistic.getMax()==null?"0.00":statistic.getMax().setScale(BIGDECIMAL_SCALE,RoundingMode.HALF_UP).toString())
                .min(statistic.getMin()==null?"0.00":statistic.getMin().setScale(BIGDECIMAL_SCALE,RoundingMode.HALF_UP).toString())
                .sum(statistic.getSum()==null?"0.00":statistic.getSum().setScale(BIGDECIMAL_SCALE,RoundingMode.HALF_UP).toString())
                .build();
    }


    /**
     * Create transaction model from request DTO
     *
     * @param transactionRequestDto user input request
     * @return created model
     */
    private Transaction createTransactionModel(TransactionRequestDto transactionRequestDto) {
        var transactionModel = new Transaction();
        transactionModel.setAmount(transactionRequestDto.getAmount());
        transactionModel.setTimeStamp(transactionRequestDto.getTimeStamp());
        return transactionModel;
    }
}
