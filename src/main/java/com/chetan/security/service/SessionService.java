package com.chetan.security.service;

import com.chetan.security.entity.Session;
import com.chetan.security.entity.User;
import com.chetan.security.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;

    public void createNewSession(User user,String refreshToken){
        List<Session> sessions = sessionRepository.findAllByUser(user);
        if(sessions.size()==SESSION_LIMIT){
            sessions.sort(Comparator.comparing(Session::getLastUsedAt));
            Session oldestSession = sessions.getFirst();
            sessionRepository.delete(oldestSession);
        }
        Session session = Session.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();

        sessionRepository.save(session);
    }

    public void validateSession(String refreshToken){
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new SessionAuthenticationException("Refresh token not valid"));
        session.setLastUsedAt(LocalDateTime.now());
    }
}
