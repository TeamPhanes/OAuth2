package com.phanes.oauth.service;

import com.phanes.oauth.config.properties.JwtProperties;
import com.phanes.oauth.domain.RefreshToken;
import com.phanes.oauth.domain.User;
import com.phanes.oauth.exception.TokenExpireException;
import com.phanes.oauth.exception.UnAuthenticateException;
import com.phanes.oauth.repository.RefreshTokenRepository;
import com.phanes.oauth.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    public String createRefreshToken(Long userId) {
        String token = UUID.randomUUID().toString();
        Instant expireTime = Instant.now().plusMillis(jwtProperties.getRefreshToken().getExpireTime().toMillis());
        Optional<RefreshToken> userRefreshToken = refreshTokenRepository.findByUserId(userId);
        RefreshToken refreshToken = userRefreshToken.isPresent() ? userRefreshToken.get().updateToken(token, expireTime) : RefreshToken.builder()
                .token(token)
                .user(User.builder().id(userId).build())
                .expireDate(expireTime)
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public String getAccessTokenIfRefreshTokenIsValid(String refreshToken) {
        RefreshToken existedToken = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new UnAuthenticateException("Invalid refresh token"));
        if (existedToken.getExpireDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(existedToken);
            throw new TokenExpireException("Refresh token expired");
        }
        String accessToken = jwtProvider.generateAccessToken(existedToken.getId());
        redisTemplate.opsForValue().set(accessToken, existedToken.getUser().getId());
        return accessToken;
    }

    public void logout(String accessToken) {
        Long userId = redisTemplate.opsForValue().get(accessToken);
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElseThrow(() -> new IllegalStateException("Invalid refresh token"));
        refreshTokenRepository.delete(refreshToken);
        redisTemplate.delete(accessToken);
    }
}