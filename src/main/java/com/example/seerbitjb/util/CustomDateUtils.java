package com.example.seerbitjb.util;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
public class CustomDateUtils {

    public static final String DATE_TIME_FORMAT ="yyyy-MM-dd'T'HH:mm:ss.SSSZ" ;

    public static LocalDate today() {
        return LocalDate.now();
    }
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static Instant nowInstant() {
        return Instant.now();
    }




    public static String formatInstantToString(Instant instant,String pattern) {
        Objects.requireNonNull(instant);

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern)
                    .withZone(ZoneId.systemDefault());
            String formattedInstant = formatter.format(instant);
            return instant.toString();
        } catch (DateTimeException e) {
            log.error("Cannot format date to string",e);
            throw new RuntimeException(e);
        }
    }



}
