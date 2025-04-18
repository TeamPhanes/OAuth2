package com.phanes.oauth.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleProfileResponse {
    private String id;
    private String email;
    private boolean verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
}