package com.api.auth.oauth2.service;

import com.api.auth.oauth2.domain.Authorization;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface Oauth2NaverService {
    Authorization getAccessToken(String code, String state);

    Map<String, Object> getNaverUserInfo(String accessToken) throws Exception;
}
