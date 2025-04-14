package com.phanes.oauth.controller;

import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.exception.StateNotFoundException;
import com.phanes.oauth.service.OAuth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Value("${frontend.url}")
    private String frontendUrl;

    @GetMapping("/{socialType}")
    public void socialLoginToProvider(@PathVariable("socialType") String socialType, HttpServletResponse response) throws IOException {
        String state = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(state, true, Duration.ofMinutes(5));
        SocialType socialTypeEnum = SocialType.valueOf(socialType.toUpperCase(Locale.KOREA));

        String authUrl = oauth2Service.getProviderUrl(socialTypeEnum, state);
        log.info("url = {}", authUrl);
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
        String accessToken = switch (socialTypeEnum) {
            case NAVER -> oauth2Service.loginIntoNaver(code, state, socialTypeEnum);
            case GOOGLE -> oauth2Service.loginIntoGoogle(code, socialTypeEnum);
            case KAKAO -> oauth2Service.loginIntoKakao(code, socialTypeEnum);
        };
        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(30 * 60);
        response.addCookie(accessTokenCookie);

        response.sendRedirect(frontendUrl);
    }
}