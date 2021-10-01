package com.api.auth.security.service;

import com.api.auth.security.domain.Authorization;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface Oatuh2KakaoService {
    Authorization getAccessToken(String code);

    Map<String, Object> getKakaoUserInfo(String accessToken) throws Exception;
}
