package com.phanes.oauth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("oauth.naver")
public class NaverProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
