package com.chetan.security.repository;

import com.chetan.security.entity.Session;
import com.chetan.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

    List<Session> findAllByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
