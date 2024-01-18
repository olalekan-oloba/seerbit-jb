package com.example.seerbitjb.transaction;

import com.example.seerbitjb.config.PropertiesConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.stream.Stream;

import static com.example.seerbitjb.util.CustomDateUtils.nowInstant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionRepositoryTest {

    /**
     * System under test (SUT)
     */
    @InjectMocks
    TransactionRepositoryImpl repository;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)//support method call chaining
    PropertiesConfig propertiesConfig;
    @BeforeEach
    void setUp() throws Exception {
        //reset repository data
        reset();
    }

    @AfterEach
    void setUpAfterEach() throws Exception {
        //reset repository data
        reset();
    }
    private void reset() {
        repository.setTransactions(new ArrayList<>());
        repository.setTransactionSum(null);
        repository.setTransactionMax(null);
        repository.setTransactionMin(null);
        repository.setTransactionCount(0);
    }


    @Test
    @DisplayName("save new transaction successfully")
    void givenTransactionsEmpty_WhenSaveNewTransaction_ShouldSucced() throws Exception {
        //arrange
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(10.55));
        newTransaction.setTimeStamp(nowInstant());
        //act
        this.repository.save(newTransaction);
        //assert
        assertThat(repository.getTransactions().size(), is(equalTo(1)));
    }

    @RepeatedTest(2)
    @DisplayName("update transactions sum successfully")
    void givenTransactionsExistOrEmpty_WhenSaveNewTransaction_ShouldUpdateTransactionsSum(RepetitionInfo repetitionInfo) throws Exception {
        //arrange
        final int EMPTY_TRANSACTIONS=1;
        final int EXISTING_TRANSACTIONS=2;

        switch (repetitionInfo.getCurrentRepetition()) {
            case EMPTY_TRANSACTIONS -> {

            }
            case EXISTING_TRANSACTIONS -> {
                //create existing txn
                var existingTxn=new Transaction();
                existingTxn.setAmount(BigDecimal.valueOf(10.55));
                existingTxn.setTimeStamp(nowInstant());
                //add existingTxn to transaction list
                repository.getTransactions().add(existingTxn);
                repository.setTransactionSum(existingTxn.getAmount());
            }
        }
        //new transaction
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(50.00));
        newTransaction.setTimeStamp(nowInstant());

        //act
        this.repository.save(newTransaction);

        //assert
        //transaction sum updated
        switch (repetitionInfo.getCurrentRepetition()) {
            case EMPTY_TRANSACTIONS -> {
                //sum equal to new transaction amount
                assertThat(repository.getTransactionSum(), is(equalTo(newTransaction.getAmount())));
            }
            case EXISTING_TRANSACTIONS -> {
                assertThat(repository.getTransactionSum(), is(equalTo(BigDecimal.valueOf(60.55))));
            }
        }
    }


    //update transaction max
    @Test
    @DisplayName("update empty transaction max successfully")
    void givenTransactionMaxDoeNotExist_WhenSaveNewTransaction_ShouldMakeNewTransactionMax() throws Exception {
        //arrange
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(10.55));
        newTransaction.setTimeStamp(nowInstant());
        //act
        this.repository.save(newTransaction);
        //assert
        assertThat(repository.getTransactionMax(), is(equalTo(newTransaction.getAmount())));
    }

    @Test
    @DisplayName("update transaction max with new value successfully")
    void givenTransactionMaxDoesExist_WhenSaveNewTransactionWithAmountGreaterThanMax_ShouldMakeNewTransactionMax() throws Exception {
        //arrange
        //existing trasaction max
        repository.setTransactionMax(BigDecimal.valueOf(5.00));
        //new transaction
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(10.55));
        newTransaction.setTimeStamp(nowInstant());
        //act
        this.repository.save(newTransaction);
        //assert new transaction amount is new max
        assertThat(repository.getTransactionMax(), is(equalTo(newTransaction.getAmount())));
    }


    @Test
    @DisplayName("unchange transaction max when new lower than existing max")
    void givenTransactionMaxDoesExist_WhenSaveNewTransactionWithAmountLowerThanMax_ShouldNotUpdateMax() throws Exception {
        //arrange
        //existing trasaction max
        repository.setTransactionMax(BigDecimal.valueOf(20.00));
        //new transaction
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(10.00));
        newTransaction.setTimeStamp(nowInstant());
        //act
        this.repository.save(newTransaction);
        //assert transaction max is unchanged
        assertThat(repository.getTransactionMax(), is(equalTo(repository.getTransactionMax())));
    }


    @Test
    @DisplayName("unchange transaction max when new lower than existing max")
    void givenTransactionMaxDoesExist_WhenSaveNewTransactionEqualToMax_ShouldNotUpdateMax() throws Exception {
        //arrange
        //existing trasaction max
        repository.setTransactionMax(BigDecimal.valueOf(20.00));
        //new transaction
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(20.00));
        newTransaction.setTimeStamp(nowInstant());
        //act
        this.repository.save(newTransaction);
        //assert transaction max is unchanged
        assertThat(repository.getTransactionMax(), is(equalTo(repository.getTransactionMax())));
    }


    //update transaction min
    @Test
    @DisplayName("update empty transaction min successfully")
    void givenTransactionMinDoeNotExist_WhenSaveNewTransaction_ShouldMakeNewTransactionMin() throws Exception {
        //arrange
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(10.55));
        newTransaction.setTimeStamp(nowInstant());
        //act
        this.repository.save(newTransaction);
        //assert
        assertThat(repository.getTransactionMin(), is(equalTo(newTransaction.getAmount())));
    }

    @ParameterizedTest
    @MethodSource({
            "newTransactionAmountProvider"
    })
    @DisplayName("update existing min transaction successfully")
    void givenTransactionMinDoesExist_WhenSaveNewTransaction_ShouldProcessMinTransactionSuccefully(BigDecimal newTransactionAmount,BigDecimal currentMin,BigDecimal expectedMin) throws Exception {
        //arrange
        repository.setTransactionMin(currentMin);
        var newTransaction=new Transaction();
        newTransaction.setAmount(newTransactionAmount);
        newTransaction.setTimeStamp(nowInstant());
        //act
        this.repository.save(newTransaction);
        //assert
        assertThat(repository.getTransactionMin(), is(equalTo(expectedMin)));
    }


    static Stream<Arguments> newTransactionAmountProvider() {
        //argument param format {newTransactionAmount, currMinimum,expectedMin}
        return Stream.of(
                //new txn amount less than curr min, should set min txn to new txn value
                arguments(BigDecimal.valueOf(20.00),BigDecimal.valueOf(30.00),BigDecimal.valueOf(20.00)),
                //new txn amount greater than  min, should not update
                arguments(BigDecimal.valueOf(30.00),BigDecimal.valueOf(20.00),BigDecimal.valueOf(20.00)),
                //new txn amount equal  min, should not update
                arguments(BigDecimal.valueOf(20.00),BigDecimal.valueOf(20.00),BigDecimal.valueOf(20.00))
        );
    }


    @RepeatedTest(2)
    @DisplayName("update transactions count successfully")
    void givenTransactionsExistOrEmpty_WhenSaveNewTransaction_ShouldUpdateTransactionsCount(RepetitionInfo repetitionInfo) throws Exception {
        //arrange
        final int EMPTY_TRANSACTIONS=1;
        final int EXISTING_TRANSACTIONS=2;

        switch (repetitionInfo.getCurrentRepetition()) {
            case EMPTY_TRANSACTIONS -> {

            }
            case EXISTING_TRANSACTIONS -> {
                //create existing txn
                var existingTxn=new Transaction();
                existingTxn.setAmount(BigDecimal.valueOf(10.55));
                existingTxn.setTimeStamp(nowInstant());
                //add existingTxn to transaction list
                repository.getTransactions().add(existingTxn);
                repository.setTransactionCount(1);
            }
        }
        //new transaction
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(50.00));
        newTransaction.setTimeStamp(nowInstant());

        //act
        this.repository.save(newTransaction);

        //assert
        //transaction count updated
        switch (repetitionInfo.getCurrentRepetition()) {
            case EMPTY_TRANSACTIONS -> {
                //count equal to new transaction amount
                assertThat(repository.getTransactionCount(), is(equalTo(1L)));
            }
            case EXISTING_TRANSACTIONS -> {
                assertThat(repository.getTransactionCount(), is(equalTo(2L)));
            }
        }
    }


    @Test
    @DisplayName("should not update txn statistics when txn date older than defined txn age")
    void givenTransactionStatisticsExist_WhenSaveNewTransactionWithDateOlderThan30Sec_ShouldNotUpdateTxnStatistics() throws Exception {
        //arrange
        //existing trasaction max
        repository.setTransactionMax(BigDecimal.valueOf(5.00));
        repository.setTransactionMin(BigDecimal.valueOf(5.00));
        repository.setTransactionCount(1L);
        repository.setTransactionSum(BigDecimal.valueOf(5.00));

        when(propertiesConfig.getApp().getTransactionDateAge()).thenReturn(30);

        //new transaction
        var newTransaction=new Transaction();
        newTransaction.setAmount(BigDecimal.valueOf(10.55));
        //txn date made 32secs ago older than defined transaction date age 30secs
        newTransaction.setTimeStamp(nowInstant().minusSeconds(31));
        //act
        this.repository.save(newTransaction);
        //assert existing txn stats not changed
        assertThat(repository.getTransactionMax(), is(equalTo(BigDecimal.valueOf(5.00))));
        assertThat(repository.getTransactionMin(), is(equalTo(BigDecimal.valueOf(5.00))));
        assertThat(repository.getTransactionCount(), is(equalTo(1L)));
        assertThat(repository.getTransactionSum(), is(equalTo(BigDecimal.valueOf(5.00))));
    }

    @Test
    void givenStaticsExist_WhenGetStatics_ShouldSucced() throws Exception {
        //arrange
        repository.setTransactionMax(BigDecimal.valueOf(40.00).setScale(2, RoundingMode.HALF_UP));
        repository.setTransactionMin(BigDecimal.valueOf(30.00).setScale(2, RoundingMode.HALF_UP));
        repository.setTransactionSum(BigDecimal.valueOf(140.00).setScale(2, RoundingMode.HALF_UP));
        repository.setTransactionCount(10L);
        //act
        var statistic=this.repository.getStatistics();
        //assert
       //avrg is sum divided by count
        assertThat(statistic.getAvg(), is(equalTo(BigDecimal.valueOf(14.00).setScale(2, RoundingMode.HALF_UP))));
        assertThat(statistic.getMax(), is(equalTo(BigDecimal.valueOf(40.00).setScale(2, RoundingMode.HALF_UP))));
        assertThat(statistic.getMin(), is(equalTo(BigDecimal.valueOf(30.00).setScale(2, RoundingMode.HALF_UP))));
        assertThat(statistic.getSum(), is(equalTo(BigDecimal.valueOf(140.00).setScale(2, RoundingMode.HALF_UP))));
        assertThat(statistic.getCount(), is(equalTo(10L)));
    }


    @Test
    @DisplayName("should delete txns successfully")
    void givenTransactionsExist_WhenSaveNewTransaction_ShouldNotUpdateTxnStatistics() throws Exception {
        //arrange
        var existingTxn=new Transaction();
        existingTxn.setAmount(BigDecimal.valueOf(5.00));
        existingTxn.setTimeStamp(nowInstant());
        //add existingTxn to transaction list
        repository.getTransactions().add(existingTxn);

        repository.setTransactionMax(BigDecimal.valueOf(5.00));
        repository.setTransactionMin(BigDecimal.valueOf(5.00));
        repository.setTransactionCount(1L);
        repository.setTransactionSum(BigDecimal.valueOf(5.00));

        //act
        this.repository.deleteTransactions();
        //assert stats cleared
        assertThat(repository.getTransactionMax(), is(equalTo(BigDecimal.ZERO)));
        assertThat(repository.getTransactionMin(), is(equalTo(BigDecimal.ZERO)));
        assertThat(repository.getTransactionCount(), is(equalTo(0L)));
        assertThat(repository.getTransactionSum(), is(equalTo(BigDecimal.ZERO)));
        assertThat(repository.getTransactions(), is(equalTo(null)));
    }

}
