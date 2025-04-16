package com.phanes.oauth.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.phanes.oauth.client.*;
import com.phanes.oauth.domain.User;
import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.dto.*;
import com.phanes.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final NaverTokenClient naverTokenClient;
    private final NaverProfileClient naverProfileClient;
    private final GoogleTokenClient googleTokenClient;
    private final GoogleProfileClient googleProfileClient;
    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoProfileClient kakaoProfileClient;
    private final RedisTemplate<String, Long> redisTemplate;
    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${naver.redirect.uri}")
    private String NAVER_REDIRECT_URI;
    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.redirect.uri}")
    private String GOOGLE_REDIRECT_URI;
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${kakao.redirect.uri}")
    private String KAKAO_REDIRECT_URI;

    public String getProviderUrl(SocialType socialType, String state) {
        return switch (socialType) {
            case GOOGLE -> UriComponentsBuilder
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
            case KAKAO -> UriComponentsBuilder
                    .fromUriString("https://kauth.kakao.com/oauth/authorize")
                    .queryParam("response_type", "code")
                    .queryParam("client_id", KAKAO_CLIENT_ID)
                    .queryParam("redirect_uri", KAKAO_REDIRECT_URI)
                    .queryParam("state", state)
                    .build()
                    .toUriString();
            case NAVER -> UriComponentsBuilder
                    .fromUriString("https://nid.naver.com/oauth2.0/authorize")
                    .queryParam("response_type", "code")
                    .queryParam("client_id", NAVER_CLIENT_ID)
                    .queryParam("redirect_uri", NAVER_REDIRECT_URI)
                    .queryParam("state", state)
                    .build()
                    .toUriString();
        };
    }

    public String loginIntoNaver(String code, String state, SocialType socialTypeEnum) {
        NaverTokenResponse tokenResponse = naverTokenClient.getToken("authorization_code", NAVER_CLIENT_ID, NAVER_CLIENT_SECRET, code, state);
        NaverProfileResponse profileResponse = naverProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        NaverProfile profile = profileResponse.getResponse();
        User user = saveUserIfNotExists(profile.getProfileImage(), profile.getId(), socialTypeEnum, tokenResponse.getRefreshToken());

        redisTemplate.opsForValue().set(tokenResponse.getAccessToken(), user.getId());

        return tokenResponse.getAccessToken();
    }

    public String loginIntoGoogle(String code, SocialType socialTypeEnum) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", GOOGLE_CLIENT_ID);
        formData.add("client_secret", GOOGLE_CLIENT_SECRET);
        formData.add("redirect_uri", GOOGLE_REDIRECT_URI);
        formData.add("code", code);

        GoogleTokenResponse tokenResponse = googleTokenClient.getToken(formData);
        GoogleProfileResponse profile = googleProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        User user = saveUserIfNotExists(profile.getPicture(), profile.getId(), socialTypeEnum, tokenResponse.getRefreshToken());

        redisTemplate.opsForValue().set(tokenResponse.getAccessToken(), user.getId());

        return tokenResponse.getAccessToken();
    }

    public String loginIntoKakao(String code, SocialType socialTypeEnum) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", KAKAO_CLIENT_ID);
        formData.add("client_secret", KAKAO_CLIENT_SECRET);
        formData.add("redirect_uri", KAKAO_REDIRECT_URI);
        formData.add("code", code);

        KakaoTokenResponse tokenResponse = kakaoTokenClient.getToken(formData);
        KakaoProfileResponse profileResponse = kakaoProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        KakaoProfileResponse.KakaoProfile.Profile profile = profileResponse.getKakao_account().getProfile();
        User user = saveUserIfNotExists(profile.getThumbnailImageUrl(), String.valueOf(profileResponse.getId()), socialTypeEnum, tokenResponse.getRefreshToken());

        redisTemplate.opsForValue().set(tokenResponse.getAccessToken(), user.getId());

        return tokenResponse.getAccessToken();
    }

    private User saveUserIfNotExists(String profileImage, String socialId, SocialType socialType, String refreshToken) {
        Optional<User> savedUser = userRepository.findBySocialIdAndSocialType(socialId, socialType);
        if (savedUser.isPresent()) {
            return savedUser.get();
        }
        User user = User.builder()
                .nickname(NanoIdUtils.randomNanoId())
                .profileImage(profileImage)
                .socialId(socialId)
                .socialType(socialType)
                .refreshToken(refreshToken)
                .build();
        return userRepository.save(user);
    }
}