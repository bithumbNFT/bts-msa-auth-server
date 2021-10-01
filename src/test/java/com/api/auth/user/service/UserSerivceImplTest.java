package com.api.auth.user.service;

import com.api.auth.security.service.SecurityTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

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