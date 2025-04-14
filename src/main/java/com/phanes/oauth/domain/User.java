package com.phanes.oauth.domain;

import com.phanes.oauth.domain.enums.SocialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String nickname;
    private String profileImage;
    private String socialId;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String refreshToken;
}
