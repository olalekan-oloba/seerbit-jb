package com.example.seerbitjb.apiresponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ApiDataResponse
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiDataResponse<T> {

  private HttpStatus status;
  @Nullable
  private String message;
  @Nullable
  private String errorCode;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  @Nullable
  private String debugMessage;
  @Nullable
  private List<ApiSubError> subErrors;
  @Nullable
  private T data;

  @SuppressWarnings("NullAway")
  private ApiDataResponse() {
    timestamp = LocalDateTime.now();
  }

  public ApiDataResponse(HttpStatus status) {
    this();
    this.status = status;
  }

  private void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }

  private void addValidationError(String field, Object rejectedValue, String message) {
    addSubError(new ApiValidationError(field, rejectedValue, message));
  }

  private void addValidationError(FieldError fieldError) {
    this.addValidationError(fieldError.getField(), fieldError.getRejectedValue(),
        fieldError.getDefaultMessage());
  }

  public void addValidationErrors(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationError);
  }


}