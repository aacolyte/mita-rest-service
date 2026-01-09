package com.mita.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Catching exceptions from services
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex){
        return Map.of("error", ex.getMessage());
    }

    // Catching database constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDuplicate(DataIntegrityViolationException ex) {
        String message = "Invalid data";

        if (ex.getMostSpecificCause().getMessage().contains("categories.name")) {
            message = "Category with this name already exists";
        }
        if (ex.getMostSpecificCause().getMessage().contains("items.title")) {
            message = "Item with this title already exists";
        }
        return Map.of("error", message);
    }

    // JSON is malformed, type mismatch
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleJsonParseError(HttpMessageNotReadableException ex) {
        return Map.of("error", "Invalid request body. Check data types.");
    }

}
