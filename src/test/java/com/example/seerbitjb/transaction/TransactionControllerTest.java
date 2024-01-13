package com.example.seerbitjb.transaction;


import com.example.seerbitjb.config.PropertiesConfig;
import com.example.seerbitjb.util.TestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static com.example.seerbitjb.util.CustomDateUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * web layer unit test for transaction controller
 */
@WebMvcTest(TransactionControllerApi.class)
public class TransactionControllerTest {

    @MockBean
    TransactionService postTransactionService;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    PropertiesConfig propertiesConfig;
    @Autowired
    protected MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource({
            "invalidJsonProvider"
    })
    void shouldFail_With400_WhenPostTransactionWithInvalidJson(String requestPayload) throws Exception {
        //input
        mockMvc.perform(post("/transactions").contentType("application/json").content(requestPayload))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    static Stream<Arguments> invalidJsonProvider() {

        return Stream.of(
                //invalid json string missing close curly brace
                arguments("""
                        {
                            "amount": "",
                            "timeStamp": ""
                        """),
                //invalid json string missing open curly brace
                arguments("""
                            "amount": "",
                             "timeStamp": ""
                            }
                        """)

        );
    }

    @ParameterizedTest
    @MethodSource({
            "nonParseableFieldsProvider"
    })
    @DisplayName("Fail on invalid/non parseable fields")
    void shouldFail_With422_WhenPostTransactionWithNonParsableFields(String requestPayload) throws Exception {
        //input
        var resultActions = this.performPostTransactionRequest(requestPayload);
        resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    static Stream<Arguments> nonParseableFieldsProvider() {
        //argument format {jsonPayload}
        return Stream.of(
                arguments("{\"amount\": \"stringAmnt\",\""+nowInstant()+"\": \"\"}"), //arbitrary string amount
                arguments("{\"amount\": \"12.3343\",\"timeStamp\": \"xxxx\"}"), //arbitary timestamp string supplied
                arguments("{\"amount\": \"12.3343\",\"timeStamp\": \"2018/07/17T09:59:51.312Z\"}"), // invalid date format
                arguments("{\"amount\": \"12.3343\",\"timeStamp\": \"2018-07-17\"}"), // only date supplied
                arguments("{\"amount\": \"12.3343\",\"timeStamp\": \"\"}"), // empty timestamp
                arguments("{\"amount\": \"\",\"timeStamp\":\"" + nowInstant() + "\"}") //empty string amount
        );
    }


    @ParameterizedTest
    @MethodSource({
            "txnDateInFutureProvider"
    })
    @DisplayName("Fail on transaction date in future")
    void shouldFail_With422_WhenPostTransactionWithTransactionDateInFuture(String requestPayload) throws Exception {
        //input
        var resultActions = this.performPostTransactionRequest(requestPayload);
        resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    static Stream<Arguments> txnDateInFutureProvider() {
        return Stream.of(
                arguments("{\"amount\": \"12.3343\",\"timeStamp\":\"" + formatInstantToString(nowInstant().plus(1, ChronoUnit.DAYS), DATE_TIME_FORMAT) + "\"}"), //txn date 1 day after current date
                arguments("{\"amount\": \"12.3343\",\"timeStamp\":\"" + formatInstantToString(nowInstant().plus(21, ChronoUnit.DAYS), DATE_TIME_FORMAT) + "\"}") //txn date 2 day after current date
        );
    }


    @Test
    @DisplayName("create transaction successfully")
    void shouldSucceed_WhenPostTransaction_WithValidInput() throws Exception {
        //arrange
        var requestTxnDate = nowInstant();
        var dto = TransactionRequestDto.builder()
                .amount(BigDecimal.valueOf(10))
                .timeStamp(requestTxnDate)
                .build();
        doNothing().when(postTransactionService).createTransaction(any(TransactionRequestDto.class));

        //act
        var resultAction = this.performPostTransactionRequest(TestUtils.asJsonString(dto));

        //assert
        resultAction.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Resource created successfully"))
                .andDo(print());
        //assert empty body
        assertEmptyBody(resultAction.andReturn());
         //assert
        var transactionCaptor = ArgumentCaptor.forClass(TransactionRequestDto.class);
        verify(postTransactionService, times(1)).createTransaction(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getAmount(), is(equalTo(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP))));
        assertThat(transactionCaptor.getValue().getTimeStamp(), is(equalTo(requestTxnDate)));
    }


    @ParameterizedTest
    @MethodSource({
            "txnOlderThanProvider"
    })
    @DisplayName("should succeed and return 204 with trx older than defined txn age")
    void shouldSucceedAndReturn204_WhenPostTransaction_WithOlderTransactionDate(String requestPayload) throws Exception {
        //act
        var resultActions = this.performPostTransactionRequest(requestPayload);
        when(propertiesConfig.getApp().getTransactionDateAge()).thenReturn(30);
        //assert
        resultActions.andExpect(status().isNoContent())
                .andDo(print());
        //assert empty body
        assertEmptyBody(resultActions.andReturn());
    }


    static Stream<Arguments> txnOlderThanProvider() throws JSONException {

        JSONObject jo = new JSONObject();
        jo.put("amount", "12.3343");
        jo.put("timeStamp", formatInstantToString(nowInstant().minusSeconds(32), DATE_TIME_FORMAT));

        return Stream.of(
                arguments(jo.toString())
        );
    }


    private void assertEmptyBody(MvcResult mvcResult) throws UnsupportedEncodingException {
        String body = TestUtils.objectFromResponseStr(mvcResult.getResponse().getContentAsString(), "$.data").toString();
        assertThat(body, is(equalTo("{}")));
    }


    ResultActions performPostTransactionRequest(String dto) throws Exception {
        return mockMvc.perform(post("/transactions").contentType("application/json").content(dto))
                .andDo(print());
    }

}

