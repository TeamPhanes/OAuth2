package com.phanes.oauth.service;

import com.phanes.oauth.domain.RefreshToken;
import com.phanes.oauth.domain.User;
import com.phanes.oauth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refresh-token.expire_time}")
    private Long refreshTokenExpiration;

    public String createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(UUID.randomUUID().toString())
                .user(User.builder().id(userId).build())
                .expireDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
