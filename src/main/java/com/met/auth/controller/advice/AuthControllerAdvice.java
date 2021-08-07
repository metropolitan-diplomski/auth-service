package com.met.auth.controller.advice;

import com.met.auth.AuthServiceApplication;
import com.met.auth.exception.AuthServiceException;
import com.met.auth.exception.ErrorCode;
import com.met.auth.exception.ErrorResponse;
import io.micrometer.core.instrument.config.validate.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AuthControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {AuthServiceException.class})
    protected ResponseEntity<ErrorResponse> handleNotFound(
            AuthServiceException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value
            = {IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ILLEGAL_ARGUMENT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value
            = {ValidationException.class})
    protected ResponseEntity<ErrorResponse> handleNotFound(
            ValidationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_EXCEPTION, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
