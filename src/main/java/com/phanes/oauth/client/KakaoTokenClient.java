package com.phanes.oauth.client;

import com.phanes.oauth.dto.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoTokenClient", url = "https://kauth.kakao.com")
public interface KakaoTokenClient {

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenResponse getToken(@RequestBody MultiValueMap<String, String> form);
}