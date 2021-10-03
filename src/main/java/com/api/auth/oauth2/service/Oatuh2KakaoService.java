package com.api.auth.oauth2.service;

import com.api.auth.oauth2.domain.Authorization;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface Oatuh2KakaoService {
    Authorization getAccessToken(String code);

    Map<String, Object> getKakaoUserInfo(String accessToken) throws Exception;
}
