package com.phanes.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NaverProfile {

    private String id;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String birthday;
    @JsonProperty("profile_image")
    private String profileImage;
    private String birthyear;
    private String mobile;
}
