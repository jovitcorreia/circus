package com.castanhocorreia.circus.controller;

import com.castanhocorreia.circus.data.ErrorData;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.requests.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestControllerAdvice
@RestController
public class HandlerController {
  @ExceptionHandler
  public ResponseEntity<ErrorData> handleException(MethodArgumentNotValidException exception) {
    List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    List<ErrorData.ErrorDetails> errorDetails = new ArrayList<>();
    for (FieldError fieldError : errors) {
      ErrorData.ErrorDetails error = new ErrorData.ErrorDetails();
      error.setFieldName(fieldError.getField());
      error.setMessage(fieldError.getDefaultMessage());
      errorDetails.add(error);
    }
    ErrorData errorData = new ErrorData();
    errorData.setErrors(errorDetails);
    log.error(errorData);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorData);
  }
}
