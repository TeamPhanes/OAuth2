package com.phanes.oauth.client;

import com.phanes.oauth.dto.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "googleTokenClient", url = "https://oauth2.googleapis.com")
public interface GoogleTokenClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse getToken(@RequestBody MultiValueMap<String, String> form);
}