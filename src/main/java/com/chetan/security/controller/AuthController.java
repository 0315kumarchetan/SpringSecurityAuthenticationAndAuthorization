package com.chetan.security.controller;

import com.chetan.security.dtos.auth.LoginRequest;
import com.chetan.security.dtos.auth.LoginResponse;
import com.chetan.security.dtos.auth.SignupRequest;
import com.chetan.security.dtos.auth.SignupResponse;
import com.chetan.security.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;

    @PostMapping("/signUp")
    public SignupResponse signUp(@RequestBody SignupRequest signupRequest){
        return authService.signup(signupRequest);
    }

    @PostMapping("/logIn")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
