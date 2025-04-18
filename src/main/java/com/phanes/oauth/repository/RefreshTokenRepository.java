package com.phanes.oauth.repository;

import com.phanes.oauth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String refreshToken);
    Optional<RefreshToken> findByUserId(Long userId);
}
