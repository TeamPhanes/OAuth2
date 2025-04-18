package com.phanes.oauth.domain;

import com.phanes.oauth.domain.enums.SocialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String profileComment;
    private String profileImage;
    private String gender;
    private String email;
    private String socialId;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    @ColumnDefault("0")
    private Boolean genderMark;
    @ColumnDefault("0")
    private Boolean emailMark;
}
