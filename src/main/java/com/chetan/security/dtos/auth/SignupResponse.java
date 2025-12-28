package com.chetan.security.dtos.auth;


public record SignupResponse(
         String token,
         Long id,
         String name
) {
}
