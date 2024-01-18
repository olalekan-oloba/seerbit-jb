package com.example.seerbitjb.apiresponse;

import jakarta.annotation.Nullable;

public class ApiValidationError extends ApiSubError {
  private String field;
  @Nullable
  private Object rejectedValue;
  private String message;


  public ApiValidationError(String field, String message) {
    this.field = field;
    this.message = message;
  }

  public ApiValidationError(String field, Object rejectedValue, String message) {
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public  @Nullable Object getRejectedValue() {
    return rejectedValue;
  }

  public void setRejectedValue(@Nullable Object rejectedValue) {
    this.rejectedValue = rejectedValue;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
