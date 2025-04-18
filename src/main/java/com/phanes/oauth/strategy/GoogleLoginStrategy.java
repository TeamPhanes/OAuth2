package com.phanes.oauth.strategy;

import com.phanes.oauth.client.GoogleProfileClient;
import com.phanes.oauth.client.GoogleTokenClient;
import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.dto.GoogleProfileResponse;
import com.phanes.oauth.dto.GoogleTokenResponse;
import com.phanes.oauth.vo.SocialProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleLoginStrategy implements SocialLoginStrategy {

    private final GoogleTokenClient googleTokenClient;
    private final GoogleProfileClient googleProfileClient;
    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.redirect.uri}")
    private String GOOGLE_REDIRECT_URI;

    @Override
    public String getSocialUrl(String state) {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("response_type", "code")
                .queryParam("client_id", GOOGLE_CLIENT_ID)
                .queryParam("redirect_uri", GOOGLE_REDIRECT_URI)
                .queryParam("scope", "email profile")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.GOOGLE;
    }

    @Override
    public SocialProfile getProfile(String code, String state) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", GOOGLE_CLIENT_ID);
        formData.add("client_secret", GOOGLE_CLIENT_SECRET);
        formData.add("redirect_uri", GOOGLE_REDIRECT_URI);
        formData.add("code", code);

        GoogleTokenResponse tokenResponse = googleTokenClient.getToken(formData);
        GoogleProfileResponse profile = googleProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        return SocialProfile.builder()
                .socialId(profile.getId())
                .profileImage(profile.getPicture())
                .socialType(SocialType.GOOGLE)
                .build();
    }
}
