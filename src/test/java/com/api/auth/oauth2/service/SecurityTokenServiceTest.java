package com.api.auth.oauth2.service;

import com.api.auth.oauth2.domain.SecurityToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class SecurityTokenServiceTest {
    @Mock
    private SecurityTokenService securityTokenService;


    @BeforeEach
    void setUp() {
        securityTokenService = new SecurityTokenService();
    }

    @Test
    void getEmail() {
        SecurityToken token = securityTokenService.generateToken("test", "test");
        String email = securityTokenService.getEmail(token.getToken());
        assertThat(email, is(equalTo("test")));
    }

    @Test
    void checkExpiredRefreshToken() {
        SecurityToken token = securityTokenService.generateToken("test", "test");
        boolean expired = securityTokenService.checkExpiredRefreshToken(token.getToken());
        Claims parseInfo = Jwts.parser().setSigningKey("test").parseClaimsJws(token.getToken()).getBody();
        if(expired){
            boolean isExpired = parseInfo.getExpiration().after(new Date());
            assertThat(isExpired,is(expired));
        }else{
            boolean isExpired = parseInfo.getExpiration().before(new Date());
            assertThat(isExpired,is(expired));
        }

    }
}