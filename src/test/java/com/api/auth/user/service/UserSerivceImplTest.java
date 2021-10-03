package com.api.auth.user.service;

import com.api.auth.oauth2.service.SecurityTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class UserSerivceImplTest {
    @Mock
    private SecurityTokenService securityTokenService;


    @BeforeEach
    void setUp() {
        securityTokenService = new SecurityTokenService();
    }

    @Test
    void refreshService() {
    }
}