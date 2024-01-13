package com.example.seerbitjb.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.example.seerbitjb.util.CustomDateUtils.nowInstant;

public class Util {

    public static boolean olderThanAge(Instant transactionDateTimestamp, int transactionDateAge) {
        Instant currentTime = nowInstant();
        return transactionDateTimestamp.until(currentTime, ChronoUnit.SECONDS) > transactionDateAge;
    }

}
