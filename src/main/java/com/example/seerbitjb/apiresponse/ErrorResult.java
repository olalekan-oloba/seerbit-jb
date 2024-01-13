package com.example.seerbitjb.apiresponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResult {
    private final List<ApiValidationError> fieldErrors = new ArrayList<>();
    public ErrorResult(String field, String message){
        this.fieldErrors.add(new ApiValidationError(field, message));
    }
}
