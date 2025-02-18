package com.phanes.oauth.client;

import com.phanes.oauth.dto.NaverTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "NaverToken", url = "https://nid.naver.com")
public interface NaverTokenClient {
    @PostMapping("/oauth2.0/token")
    NaverTokenResponse getToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    );
}