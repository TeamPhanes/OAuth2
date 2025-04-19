package com.phanes.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String image;
    private String nickname;
    private String gender;
    private String email;
    private String comment;
    private Boolean genderMark;
    private Boolean emailMark;
}
