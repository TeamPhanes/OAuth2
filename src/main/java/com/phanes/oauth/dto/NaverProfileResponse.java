package com.phanes.oauth.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NaverProfileResponse {
    private String resultcode;
    private String message;
    private NaverProfile response;
}
