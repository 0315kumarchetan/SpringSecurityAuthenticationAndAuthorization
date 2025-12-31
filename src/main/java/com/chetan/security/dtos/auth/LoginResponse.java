package com.chetan.security.dtos.auth;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Long id,
        String name
) {
}
