package com.chetan.security.handler;

import com.chetan.security.entity.User;
import com.chetan.security.security.JwtService;
import com.chetan.security.service.SessionService;
import com.chetan.security.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;
    private final SessionService sessionService;
    @Value("${deploy.env}")
    private String deployEnv;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User user = (DefaultOAuth2User) token.getPrincipal();

        String email = user.getAttribute("email");
        User userFromDb =  userService.findByEmail(email);
        if(userFromDb==null){
            userFromDb = userService.saveUser(User.builder()
                    .username(email)
                    .build());
        }
        String accessToken = jwtService.generateAccessToken(userFromDb);
        String refreshToken = jwtService.generateRefreshToken(userFromDb);
        sessionService.createNewSession(userFromDb,refreshToken);
        Cookie cookie = new Cookie("RefreshToken",refreshToken);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;
        response.sendRedirect(frontEndUrl);

    }
}
