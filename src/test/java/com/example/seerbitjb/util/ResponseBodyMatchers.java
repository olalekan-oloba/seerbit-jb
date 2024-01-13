package com.example.seerbitjb.util;

import com.example.seerbitjb.apiresponse.ApiValidationError;
import com.example.seerbitjb.apiresponse.ErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseBodyMatchers {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResultMatcher containsError(String expectedFieldName, String expectedMessage) {
        return mvcResult -> {

            String json = mvcResult.getResponse().getContentAsString();

            //get the field errors from the api response object
            String fieldErrorsResp = TestUtils.objectFromResponseStr(json, "$.subErrors");

            var errorResult = objectMapper.readValue(fieldErrorsResp, ErrorResult.class);
            List<ApiValidationError> fieldErrors = errorResult.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getField().equals(expectedFieldName))
                    .filter(fieldError -> fieldError.getMessage().equals(expectedMessage))
                    .toList();

            assertEquals(1,fieldErrors.size());
        };
    }
}