package com.phanes.oauth.controller;

import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.exception.StateNotFoundException;
import com.phanes.oauth.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;

    @PostMapping("/state")
    public void saveNaverState(@RequestBody String state) {
        oauth2Service.saveState(state);
    }

    @GetMapping("/{socialType}/callback")
    public void socialLoginCallback(@RequestParam("code") String code, @RequestParam("state") String state, @PathVariable("socialType") String socialType) {
        String savedState = oauth2Service.findNaverState(state);
        if (!savedState.equals(state)) {
            throw new StateNotFoundException("did not found state");
        }

        SocialType socialTypeEnum = SocialType.valueOf(socialType.toUpperCase(Locale.KOREA));
        switch (socialTypeEnum) {
            case NAVER -> oauth2Service.loginIntoNaver(code, state, socialTypeEnum);
            case GOOGLE -> oauth2Service.loginIntoGoogle(code, socialTypeEnum);
            case KAKAO -> oauth2Service.loginIntoKakao(code, socialTypeEnum);
        }
    }
}