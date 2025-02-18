package com.phanes.oauth.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.phanes.oauth.client.*;
import com.phanes.oauth.domain.OAuthState;
import com.phanes.oauth.domain.User;
import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.dto.*;
import com.phanes.oauth.repository.NaverStateRepository;
import com.phanes.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final NaverStateRepository stateRepository;
    private final NaverTokenClient naverTokenClient;
    private final NaverProfileClient naverProfileClient;
    private final GoogleTokenClient googleTokenClient;
    private final GoogleProfileClient googleProfileClient;
    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoProfileClient kakaoProfileClient;
    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;
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

    public void saveState(String state) {
        stateRepository.save(OAuthState.builder().state(state).build());
    }

    public String findNaverState(String state) {
        return stateRepository.findByState(state).getState();
    }

    public void loginIntoNaver(String code, String state, SocialType socialTypeEnum) {
        NaverTokenResponse tokenResponse = naverTokenClient.getToken("authorization_code", NAVER_CLIENT_ID, NAVER_CLIENT_SECRET, code, state);
        NaverProfileResponse profileResponse = naverProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        NaverProfile profile = profileResponse.getResponse();
        saveUserIfNotExists(profile.getProfileImage(), profile.getId(), socialTypeEnum);
    }

    public void loginIntoGoogle(String code, SocialType socialTypeEnum) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", GOOGLE_CLIENT_ID);
        formData.add("client_secret", GOOGLE_CLIENT_SECRET);
        formData.add("redirect_uri", GOOGLE_REDIRECT_URI);
        formData.add("code", code);

        GoogleTokenResponse tokenResponse = googleTokenClient.getToken(formData);
        GoogleProfileResponse profile = googleProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        saveUserIfNotExists(profile.getPicture(), profile.getId(), socialTypeEnum);
    }

    public void loginIntoKakao(String code, SocialType socialTypeEnum) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", KAKAO_CLIENT_ID);
        formData.add("client_secret", KAKAO_CLIENT_SECRET);
        formData.add("redirect_uri", KAKAO_REDIRECT_URI);
        formData.add("code", code);

        KakaoTokenResponse tokenResponse = kakaoTokenClient.getToken(formData);
        KakaoProfileResponse profileResponse = kakaoProfileClient.getProfile("Bearer " + tokenResponse.getAccessToken());
        KakaoProfileResponse.KakaoProfile.Profile profile = profileResponse.getKakao_account().getProfile();
        saveUserIfNotExists(profile.getThumbnailImageUrl(), String.valueOf(profileResponse.getId()), socialTypeEnum);
    }

    private User saveUserIfNotExists(String profileImage, String socialId, SocialType socialType) {
        Optional<User> savedUser = userRepository.findBySocialIdAndSocialType(socialId, socialType);
        if (savedUser.isPresent()) {
            return savedUser.get();
        }
        User user = User.builder()
                .nickname(NanoIdUtils.randomNanoId())
                .profileImage(profileImage)
                .socialId(socialId)
                .socialType(socialType)
                .build();
        return userRepository.save(user);
    }
}