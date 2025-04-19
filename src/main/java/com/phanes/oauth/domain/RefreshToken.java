package com.phanes.oauth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private Instant expireDate;

    public RefreshToken updateToken(String token, Instant expireDate) {
        this.token = token;
        this.expireDate = expireDate;
        return this;
    }
}
