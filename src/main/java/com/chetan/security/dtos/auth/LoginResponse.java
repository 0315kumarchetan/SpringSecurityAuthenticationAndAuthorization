package com.chetan.security.dtos.auth;

public record LoginResponse(
        String token,
        Long id,
        String name
) {
}
