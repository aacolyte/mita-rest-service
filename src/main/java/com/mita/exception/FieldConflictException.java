package com.mita.exception;

public class FieldConflictException extends RuntimeException {
    private final String message;
    public FieldConflictException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
