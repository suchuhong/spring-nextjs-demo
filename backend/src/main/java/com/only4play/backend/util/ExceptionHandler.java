package com.only4play.backend.util;

import com.only4play.backend.util.exception.ApiException;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    List<String> generalErrors = new ArrayList<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      if (error instanceof FieldError) {
        FieldError fieldErr = (FieldError) error;
        String fieldName = fieldErr.getField();
        String errorMessage = fieldErr.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      } else {
        generalErrors.add(error.getDefaultMessage());
      }
    });

    HttpErrorResponse response = HttpErrorResponse.of("Unprocessable entity", 422, errors, generalErrors);

    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
  public ResponseEntity<HttpErrorResponse> handleException(ApiException e) {
    log.info("Handling ApiException: {}", e.getMessage());
    HttpErrorResponse response = HttpErrorResponse.of(e.getMessage(), e.getStatus(), e.getErrors(), null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(e.getStatus()));
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<HttpErrorResponse> handleException(BadCredentialsException e) {
    log.info("Handling BadCredentialsException: {}", e.getMessage());
    HttpErrorResponse response = HttpErrorResponse.of(e.getMessage(), 401, null, null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(401));
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  public ResponseEntity<HttpErrorResponse> handleException(Exception e) {
    log.error("Unhandled exception", e);
    HttpErrorResponse response = HttpErrorResponse.of("Unexpected error", 500);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}