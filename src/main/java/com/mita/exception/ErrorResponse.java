package com.mita.exception;

import java.time.Instant;

public record ErrorResponse(
        String error,
        int status,
        Instant timestamp,
        String path
){}
