package com.phanes.oauth.service;

import com.phanes.oauth.config.properties.JwtProperties;
import com.phanes.oauth.domain.RefreshToken;
import com.phanes.oauth.domain.User;
import com.phanes.oauth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public String createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(UUID.randomUUID().toString())
                .user(User.builder().id(userId).build())
                .expireDate(Instant.now().plusMillis(jwtProperties.getRefreshToken().getExpireTime().toMillis()))
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
