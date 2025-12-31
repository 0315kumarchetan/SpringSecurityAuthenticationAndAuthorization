package com.chetan.security.error;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ApiError(
        HttpStatus status,
        String message,
        Instant timeInstant
) {
}
