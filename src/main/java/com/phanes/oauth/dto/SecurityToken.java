package com.phanes.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SecurityToken {

    private String accessToken;
    private String refreshToken;
}
