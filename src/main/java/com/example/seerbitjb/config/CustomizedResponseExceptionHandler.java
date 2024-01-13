package com.example.seerbitjb.config;


import com.example.seerbitjb.apiresponse.ApiDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiDataResponse<?> apiResponse = new ApiDataResponse<>(HttpStatus.UNPROCESSABLE_ENTITY);
        apiResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiDataResponse<?> apiResponse = new ApiDataResponse<>(HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
