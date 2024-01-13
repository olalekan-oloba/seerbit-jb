package com.example.seerbitjb.transaction;


import com.example.seerbitjb.util.TestUtils;
import lombok.RequiredArgsConstructor;
import org.hamcrest.core.Is;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static com.example.seerbitjb.util.CustomDateUtils.nowInstant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class TransactionIntegrationTest  {

    @Autowired
    TransactionRepositoryImpl transactionRepository;
    @Autowired
    protected MockMvc mockMvc;
    HttpHeaders headers=new HttpHeaders();

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
        transactionRepository.setTransactions(null);
        transactionRepository.setTransactionSum(null);
        transactionRepository.setTransactionMax(null);
        transactionRepository.setTransactionMin(null);
        transactionRepository.setTransactionCount(0);
    }


    @Test
    void givenTransactionNotExist_WhenUserPostTransactiom_ShouldSucceed() throws Exception {
        //arrange
        var dto = TransactionRequestDto.builder()
                .amount(BigDecimal.valueOf(10))
                .timeStamp(nowInstant())
                .build();
        //act
        ResultActions resultActions = this.performPostRequest(dto);

        resultActions.andExpect(status().isCreated());

        assertThat(transactionRepository.getTransactions().size(), is(equalTo(1)));
    }


    @Test
    void givenTransactionsExistForLastDefinedAge_WhenUserGetStatistics_ShouldSucceed() throws Exception {
        //arrange
        var dto = TransactionRequestDto.builder()
                .amount(BigDecimal.valueOf(10))
                .timeStamp(nowInstant())
                .build();
        this.performPostRequest(dto);
        //act
        var resultActions= mockMvc.perform(get("/statistics"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Is.is("Retrieved successfully")))
                .andExpect(jsonPath("$.data.avg").value("10.00"))
                .andExpect(jsonPath("$.data.count").value("1"))
                .andExpect(jsonPath("$.data.max").value("10.00"))
                .andExpect(jsonPath("$.data.min").value("10.00"))
                .andExpect(jsonPath("$.data.sum").value("10.00"))
                .andDo(print());
    }


    @Test
    void givenTransactionsExist_WhenDeleteTransactions_ShouldSucceed() throws Exception {
        //arrange
        var dto = TransactionRequestDto.builder()
                .amount(BigDecimal.valueOf(10))
                .timeStamp(nowInstant())
                .build();
        this.performPostRequest(dto);

        //act
        var resultActions= mockMvc.perform(delete("/transactions").contentType("application/json").content(new JSONObject().toString()));

        //assert
        resultActions.andExpect(status().isNoContent());
        assertThat(transactionRepository.getTransactions(), is(equalTo(null)));
    }


    private ResultActions performPostRequest(TransactionRequestDto dto) throws Exception {
        return this.mockMvc
                .perform(MockMvcRequestBuilders.post(  "/transactions").content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON).headers(headers).accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }


}
