package com.example.seerbitjb.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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

}
