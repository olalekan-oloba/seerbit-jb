package com.example.seerbitjb.transaction;


import com.example.seerbitjb.apiresponse.ApiDataResponse;
import com.example.seerbitjb.apiresponse.EmptyResponseBody;
import com.example.seerbitjb.config.PropertiesConfig;
import com.example.seerbitjb.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.example.seerbitjb.util.CustomDateUtils.nowInstant;
import static com.example.seerbitjb.util.Util.olderThanAge;


@RequiredArgsConstructor
@RestController
@Tag(name ="Transaction API Controller",description = "Transaction Management")
public class TransactionControllerApi {

    private final TransactionService postTransactionService;
    private final PropertiesConfig propertiesConfig;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success")
    })
    @Operation(summary = "Post Transaction", description = "")
    @PostMapping("transactions")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<ApiDataResponse<EmptyResponseBody>> postTransaction(@Valid @RequestBody TransactionRequestDto transactionRequestDto) {
        postTransactionService.createTransaction(transactionRequestDto);
        var httpStatus = processResponseStatus(transactionRequestDto.getTimeStamp());
        return ApiResponseUtil.response(httpStatus, new EmptyResponseBody(), "Resource created successfully");
    }

    private HttpStatus processResponseStatus(Instant requestTransactionDate) {
        //get current time
        Instant currentTime = nowInstant();
        // determine http status to return based on age of transaction date
        // if transaction date is older than defined transaction date age return 204 otherwise return 201
        return olderThanAge(requestTransactionDate,propertiesConfig.getApp().getTransactionDateAge()) ? HttpStatus.NO_CONTENT : HttpStatus.CREATED;
    }


}
