package com.api.auth.user.domain;

import lombok.Getter;

@Getter
public class LoginDto {
    private String code;
    private String state;
    private String social;
}
