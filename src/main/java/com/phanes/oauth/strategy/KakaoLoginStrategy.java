package com.phanes.oauth.strategy;

import com.phanes.oauth.client.KakaoProfileClient;
import com.phanes.oauth.client.KakaoTokenClient;
import com.phanes.oauth.config.properties.KakaoProperties;
import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.dto.KakaoProfileResponse;
import com.phanes.oauth.dto.KakaoTokenResponse;
import com.phanes.oauth.dto.SocialProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoLoginStrategy implements SocialLoginStrategy {

    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoProfileClient kakaoProfileClient;
    private final KakaoProperties properties;

    @Override
    public String getSocialUrl(String state) {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    @Override
    public SocialProfile getProfile(String code, String state) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", properties.getClientId());
        formData.add("client_secret", properties.getClientSecret());
        formData.add("redirect_uri", properties.getRedirectUri());
        formData.add("code", code);

        KakaoTokenResponse tokenResponse = kakaoTokenClient.getToken(formData);
        KakaoProfileResponse profileResponse = kakaoProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        KakaoProfileResponse.KakaoProfile.Profile profile = profileResponse.getKakao_account().getProfile();
        return SocialProfile.builder()
                .socialId(String.valueOf(profileResponse.getId()))
                .profileImage(profile.getThumbnailImageUrl())
                .socialType(SocialType.KAKAO)
                .build();
    }
}
