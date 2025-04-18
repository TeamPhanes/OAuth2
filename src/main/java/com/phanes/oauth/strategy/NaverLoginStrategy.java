package com.phanes.oauth.strategy;

import com.phanes.oauth.client.NaverProfileClient;
import com.phanes.oauth.client.NaverTokenClient;
import com.phanes.oauth.config.properties.NaverProperties;
import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.dto.NaverProfile;
import com.phanes.oauth.dto.NaverProfileResponse;
import com.phanes.oauth.dto.NaverTokenResponse;
import com.phanes.oauth.dto.SocialProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverLoginStrategy implements SocialLoginStrategy {

    private final NaverTokenClient naverTokenClient;
    private final NaverProfileClient naverProfileClient;
    private final NaverProperties properties;

    @Override
    public String getSocialUrl(String state) {
        return UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }

    @Override
    public SocialProfile getProfile(String code, String state) {
        NaverTokenResponse tokenResponse = naverTokenClient.getToken("authorization_code", properties.getClientId(), properties.getClientSecret(), code, state);
        NaverProfileResponse profileResponse = naverProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        NaverProfile profile = profileResponse.getResponse();
        return SocialProfile.builder()
                .socialId(profile.getId())
                .profileImage(profile.getProfileImage())
                .socialType(SocialType.NAVER)
                .build();
    }
}
