package com.example.seerbitjb.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.seerbitjb.util.CustomDateUtils.nowInstant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceTest {

    /**
     * System under test (SUT)
     */
    @InjectMocks
    TransactionServiceImpl transactionService;
    @Mock
    TransactionRepository repository;

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        //arrange
        var requestTxnDate = nowInstant();
        var dto = TransactionRequestDto.builder()
                .amount(BigDecimal.valueOf(10.55))
                .timeStamp(requestTxnDate)
                .build();
        doNothing().when(repository).save(any(Transaction.class));
        //act
        this.transactionService.createTransaction(dto);
        //assert
        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(repository,times(1)).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getAmount(), is(equalTo(BigDecimal.valueOf(10.55))));
        assertThat(transactionCaptor.getValue().getTimeStamp(), is(equalTo(requestTxnDate)));
    }




    @Test
    void shouldGetStatisticsSuccessfully() throws Exception {

        //arrange
        var statistic=new Statistics();
        statistic.setAvg(BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP));
        statistic.setMax(BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_UP));
        statistic.setMin(BigDecimal.valueOf(30.00).setScale(2, RoundingMode.HALF_UP));
        statistic.setSum(BigDecimal.valueOf(40.00).setScale(2, RoundingMode.HALF_UP));
        statistic.setCount(10L);

        when(this.repository.getStatistics()).thenReturn(statistic);
        //act
        var statisticDetailDto=this.transactionService.getStatistics();
        //assert
        verify(repository,times(1)).getStatistics();

        assertThat(statisticDetailDto.getAvg(), is(equalTo("10.00")));
        assertThat(statisticDetailDto.getMax(), is(equalTo("20.00")));
        assertThat(statisticDetailDto.getMin(), is(equalTo("30.00")));
        assertThat(statisticDetailDto.getSum(), is(equalTo("40.00")));
        assertThat(statisticDetailDto.getCount(), is(equalTo("10")));
    }


}
