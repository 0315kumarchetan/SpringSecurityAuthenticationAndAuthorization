package com.chetan.security.service;

import com.chetan.security.dtos.auth.LoginRequest;
import com.chetan.security.dtos.auth.LoginResponse;
import com.chetan.security.dtos.auth.SignupRequest;
import com.chetan.security.dtos.auth.SignupResponse;
import com.chetan.security.entity.User;
import com.chetan.security.repository.UserRepository;
import com.chetan.security.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    @NonFinal
    @Value("${deploy.env}")
    String deployEnv;

    public SignupResponse signup(SignupRequest signupRequest){
        User userExists = userRepository.findByUsername(signupRequest.username()).orElse(null);
        if(userExists!=null) throw new RuntimeException("User already registered with username :"+signupRequest.username());
        User user = User.builder()
                .name(signupRequest.name())
                .password(passwordEncoder.encode(signupRequest.password()))
                .username(signupRequest.username())
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateAccessToken(user);
        return new SignupResponse(token,savedUser.getId(),savedUser.getName());
    }

    public LoginResponse login(LoginRequest loginRequest,HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password())
        );

        User user = (User) authentication.getPrincipal();
        if(user==null)throw new AuthenticationCredentialsNotFoundException("User principal not found");
        String accessToken =  jwtService.generateAccessToken(user);
        String refreshToken =  jwtService.generateRefreshToken(user);
        Cookie cookie = new Cookie("RefreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);
        return new LoginResponse(accessToken,refreshToken,user.getId(),user.getName());
    }

    public LoginResponse getRefreshToken( HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String refreshToken =  Arrays.stream(cookies).filter(cookie -> "RefreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()->new AuthenticationServiceException("Refresh token not found!"));

        Long  userId = jwtService.getUserIdFromRefreshToken(refreshToken);
        User user  = userRepository.findById(userId).orElseThrow();
        String accessToken =  jwtService.generateAccessToken(user);
        return new LoginResponse(accessToken,refreshToken,user.getId(),user.getName());
    }
}
