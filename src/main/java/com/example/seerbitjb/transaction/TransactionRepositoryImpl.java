package com.example.seerbitjb.transaction;

import com.example.seerbitjb.config.PropertiesConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.seerbitjb.util.Util.olderThanAge;

@Setter
@Getter
@Component
public class TransactionRepositoryImpl implements TransactionRepository {
    private List<Transaction> transactions ;
    private BigDecimal transactionSum;
    private BigDecimal transactionMax;
    private BigDecimal transactionMin;
    private long transactionCount;
    private PropertiesConfig propertiesConfig;

    public TransactionRepositoryImpl(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void save(Transaction transaction) {
        this.saveNewTransaction(transaction);
        if(!olderThanAge(transaction.getTimeStamp(),propertiesConfig.getApp().getTransactionDateAge())) {
           updateStatistics(transaction);
        }
    }

    private void updateStatistics(Transaction transaction) {
        updateTransactionSum(transaction.getAmount());
        updateTransactionMax(transaction.getAmount());
        updateTransactionMin(transaction.getAmount());
        updateTransactionCount();
    }

    private void saveNewTransaction(Transaction transaction) {
        if(transactions==null){
            transactions=new ArrayList<>();
        }
        this.transactions.add(transaction);
    }

    @Override
    public Statistics getStatistics() {
        var statistic=new Statistics();
        statistic.setMax(transactionMax);
        statistic.setMin(transactionMin);
        statistic.setCount(transactionCount);
        statistic.setSum(transactionSum);
        statistic.calculateAvrg();
        return statistic;
    }

    @Override
    public void deleteTransactions() {
        deleteAllTransactions();
        clearStatistics();
    }

    private void clearStatistics() {
        this.setTransactionCount(0);;
        this.setTransactionMin(null);
        this.setTransactionMax(null);
        this.setTransactionSum(null);
    }

    private void deleteAllTransactions() {
        this.transactions=null;
    }

    private void updateTransactionCount() {
        this.transactionCount++;
    }

    /**
     * process current minimum transation amount value. if new amount less than current
     * min update min value with new amount otherwise do nothing
     * @param amount new transaction amount
     */
    private void updateTransactionMin(BigDecimal amount) {
        if (null  == transactionMin) {
            transactionMin = amount;
        }else {
            if(amount.compareTo(transactionMin) < 0){
                transactionMin=amount;
            }
        }
    }

    /**
     * process current maximum transation amount value. if new amount greater than current
     * max update max value with new amount otherwise do nothing
     * @param amount new transaction amount
     */
    private void updateTransactionMax(BigDecimal amount) {
        if (null  == transactionMax) {
            transactionMax = amount;
        }else{
            if(amount.compareTo(transactionMax) > 0){
               transactionMax=amount;
            }
        }
    }

    private void updateTransactionSum(BigDecimal amount) {
        if (null  == transactionSum) {
            transactionSum = amount;
        } else {
            transactionSum = transactionSum.add(amount);
        }
    }
}
