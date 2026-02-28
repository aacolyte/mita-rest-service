package com.mita.controller;

import exception.ErrorResponse;
import exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Catching exceptions from services
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request){
        log.warn("Illegal argument", ex.getMessage());

        return buildError(ex.getMessage(),HttpStatus.BAD_REQUEST, request);
    }

    // Catching database constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                            HttpServletRequest request) {
        String message = "Invalid data";
        Throwable cause = ex.getMostSpecificCause();
        String text = cause.getMessage().toLowerCase();

        if (text.contains("unique") && text.contains("name")) {
            message = "Category name must be unique";
        }
        log.warn("Database constraint violation", ex.getMessage());

        return buildError(message,HttpStatus.BAD_REQUEST,request);
    }



    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex,
                                                          HttpServletRequest request) {

        log.warn("Validation error", ex.getMessage());

        return buildError(ex.getMessage(),HttpStatus.BAD_REQUEST,request);
    }




    // JSON is malformed, type mismatch
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex,
                                                              HttpServletRequest request) {

        log.warn("Invalid request body", ex.getMessage());

        return buildError("Invalid request body. Check data types",HttpStatus.BAD_REQUEST,request);
    }



    private ResponseEntity<ErrorResponse> buildError(
            String message,
            HttpStatus status,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                message,
                status.value(),
                Instant.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }
}


