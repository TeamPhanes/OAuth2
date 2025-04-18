package com.phanes.oauth.strategy;

import com.phanes.oauth.domain.enums.SocialType;
import com.phanes.oauth.vo.SocialProfile;

public interface SocialLoginStrategy {
    String getSocialUrl(String state);
    SocialType getSocialType();
    SocialProfile getProfile(String code, String state);
}
