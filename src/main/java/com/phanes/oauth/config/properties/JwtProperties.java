package com.phanes.oauth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private TokenExpiration accessToken;
    private TokenExpiration refreshToken;

    @Getter
    @Setter
    public static class TokenExpiration {
        private Duration expireTime;
    }
}
