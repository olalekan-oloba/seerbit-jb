package com.example.seerbitjb.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class JsonUtil {


    public static boolean isValidJsonRequest(Throwable ex) {
        return ex.getClass().equals(InvalidFormatException.class);
    }
}
