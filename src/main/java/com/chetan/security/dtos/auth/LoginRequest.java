package com.chetan.security.dtos.auth;

public record LoginRequest(
        String username,
        String password
) {

}
