package com.api.auth.security.service;

import com.api.auth.security.domain.Authorization;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface Oauth2NaverService {
    Authorization getAccessToken(String code, String state);

    Map<String, Object> getNaverUserInfo(String accessToken) throws Exception;
}
