package com.example.seerbitjb.transaction;


import com.example.seerbitjb.util.TestUtils;
import lombok.RequiredArgsConstructor;
import org.hamcrest.core.Is;
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
    TransactionRepository transactionRepository;
    @Autowired
    protected MockMvc mockMvc;
    HttpHeaders headers=new HttpHeaders();

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

        assertThat(1, is(equalTo(transactionRepository.getTransactions().size())));
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

    private ResultActions performPostRequest(TransactionRequestDto dto) throws Exception {
        return this.mockMvc
                .perform(MockMvcRequestBuilders.post(  "/transactions").content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON).headers(headers).accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }


}
