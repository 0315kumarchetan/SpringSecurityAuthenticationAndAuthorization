package com.chetan.security.controller;

import com.chetan.security.dtos.auth.LoginRequest;
import com.chetan.security.dtos.auth.LoginResponse;
import com.chetan.security.dtos.auth.SignupRequest;
import com.chetan.security.dtos.auth.SignupResponse;
import com.chetan.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response ){
        return authService.login(loginRequest,response);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(HttpServletRequest request){
        return authService.getRefreshToken(request);
    }
}
