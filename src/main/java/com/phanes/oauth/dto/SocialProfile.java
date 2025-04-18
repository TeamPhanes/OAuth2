package com.phanes.oauth.dto;

import com.phanes.oauth.domain.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialProfile {
    private String socialId;
    private String profileImage;
    private SocialType socialType;
    private String gender;
    private String email;
}