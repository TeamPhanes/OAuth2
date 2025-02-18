package com.phanes.oauth.client;

import com.phanes.oauth.dto.NaverProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "Naver", url = "https://openapi.naver.com")
public interface NaverProfileClient {
    @GetMapping("/v1/nid/me")
    NaverProfileResponse getProfile(@RequestHeader("Authorization") String token);
}