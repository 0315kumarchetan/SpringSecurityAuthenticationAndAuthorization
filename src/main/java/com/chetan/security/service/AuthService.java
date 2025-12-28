package com.chetan.security.service;

import com.chetan.security.dtos.auth.LoginRequest;
import com.chetan.security.dtos.auth.LoginResponse;
import com.chetan.security.dtos.auth.SignupRequest;
import com.chetan.security.dtos.auth.SignupResponse;
import com.chetan.security.entity.User;
import com.chetan.security.repository.UserRepository;
import com.chetan.security.security.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;

    public SignupResponse signup(SignupRequest signupRequest){
        User userExists = userRepository.findByUsername(signupRequest.username()).orElse(null);
        if(userExists!=null) throw new RuntimeException("User already registered with username :"+signupRequest.username());
        User user = User.builder()
                .name(signupRequest.name())
                .password(passwordEncoder.encode(signupRequest.password()))
                .username(signupRequest.username())
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new SignupResponse(token,savedUser.getId(),savedUser.getName());
    }

    public LoginResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        String token =  jwtService.generateToken(user);
        return new LoginResponse(token,user.getId(),user.getName());
    }
}
