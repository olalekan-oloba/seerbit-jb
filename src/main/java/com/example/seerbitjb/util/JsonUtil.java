package com.example.seerbitjb.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.example.seerbitjb.util.CustomDateUtils.nowInstant;

public class JsonUtil {

    public static boolean olderThanAge(Instant transactionDateTimestamp, int transactionDateAge) {
        Instant currentTime = nowInstant();
        return transactionDateTimestamp.until(currentTime, ChronoUnit.SECONDS) > transactionDateAge;
    }

    public static boolean isValidJsonRequest(Throwable ex) {
        return ex.getClass().equals(InvalidFormatException.class);
    }
}
