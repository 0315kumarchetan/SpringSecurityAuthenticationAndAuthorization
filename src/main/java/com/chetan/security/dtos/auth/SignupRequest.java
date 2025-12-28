package com.chetan.security.dtos.auth;

public record SignupRequest(
        String username,
        String password,
        String name
) {
}
