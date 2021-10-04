package com.api.auth.user.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {
    private Long id;
    private String social;
    private String name;
    private String email;
    private String picture;
    private Role role;
    private String coinWallet;

    @Builder
    public User(String name, String social, String email, String picture, Role role, String coinWallet) {
        this.name = name;
        this.social = social;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.coinWallet = coinWallet;
    }

    public User update(String name, String picture, String social) {
        this.name = name;
        this.picture = picture;
        this.social = social;
        return this;
    }
}
