package com.api.auth.oauth2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class SecurityToken {
    private String token;
    private String refreshToken;

    public SecurityToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

}
