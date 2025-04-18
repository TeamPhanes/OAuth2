package com.phanes.oauth.controller;

import com.phanes.oauth.config.properties.DomainProperties;
import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.dto.SecurityToken;
import com.phanes.oauth.exception.StateNotFoundException;
import com.phanes.oauth.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;
    private final RedisTemplate<String, Boolean> redisTemplate;
    private final DomainProperties domainProperties;

    @GetMapping("/{socialType}")
    public void socialLoginToProvider(@PathVariable("socialType") String socialType, HttpServletResponse response) throws IOException {
        String state = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(state, true, Duration.ofMinutes(5));
        SocialType socialTypeEnum = SocialType.valueOf(socialType.toUpperCase(Locale.KOREA));

        String authUrl = oauth2Service.getProviderUrl(socialTypeEnum, state);
        response.sendRedirect(authUrl);
    }

    @GetMapping("/{socialType}/callback")
    public void socialLoginCallback(@PathVariable("socialType") String socialType, @RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) throws IOException {
        boolean isValid = redisTemplate.opsForValue().get(state);

        if (Boolean.FALSE.equals(isValid)) {
            throw new StateNotFoundException("state not found");
        }
        redisTemplate.delete(state);

        SocialType socialTypeEnum = SocialType.valueOf(socialType.toUpperCase(Locale.KOREA));
        SecurityToken token = oauth2Service.login(code, state, socialTypeEnum);


        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .domain(domainProperties.getCookie())
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(domainProperties.getFrontend());
    }
}