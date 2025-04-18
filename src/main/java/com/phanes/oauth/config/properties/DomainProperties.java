package com.phanes.oauth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.domain")
public class DomainProperties {
    private String frontend;
    private String cookie;
}
