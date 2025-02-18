package com.phanes.oauth.client;

import com.phanes.oauth.dto.KakaoProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoProfileClient", url = "https://kapi.kakao.com")
public interface KakaoProfileClient {

    @GetMapping("/v2/user/me")
    KakaoProfileResponse getProfile(@RequestHeader("Authorization") String authorization);
}