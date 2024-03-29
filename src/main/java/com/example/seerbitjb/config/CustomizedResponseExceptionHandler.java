package com.example.seerbitjb.config;


import com.example.seerbitjb.apiresponse.ApiDataResponse;
import com.example.seerbitjb.util.JsonUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
@Slf4j
public class CustomizedResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,@NonNull HttpStatusCode status,@NonNull WebRequest request) {
        ApiDataResponse<?> apiResponse = new ApiDataResponse<>(HttpStatus.UNPROCESSABLE_ENTITY);
        apiResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,@NonNull HttpStatusCode status,@NonNull WebRequest request) {
        HttpStatus httpStatus;
        if(JsonUtil.isValidJsonRequest(ex.getCause())){
            httpStatus=HttpStatus.UNPROCESSABLE_ENTITY;
        }else{
            httpStatus=HttpStatus.BAD_REQUEST;
        }

        ApiDataResponse<?> apiResponse = new ApiDataResponse<>(httpStatus);
        return new ResponseEntity<>(apiResponse, httpStatus);
    }


}
