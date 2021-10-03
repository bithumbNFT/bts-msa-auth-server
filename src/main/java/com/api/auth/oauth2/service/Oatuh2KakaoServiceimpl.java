package com.api.auth.oauth2.service;

import com.api.auth.oauth2.domain.Authorization;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oatuh2KakaoServiceimpl implements Oatuh2KakaoService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoOauth2ClinetId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String frontendRedirectUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.token_uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Override
    public Authorization getAccessToken(String code) {
        String grantType = "authorization_code";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", kakaoOauth2ClinetId);
        params.add("redirect_uri", frontendRedirectUrl);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(kakaoTokenUri, request, String.class);

        Authorization authorization = null;
        try {
            authorization = objectMapper.readValue(response.getBody(), Authorization.class);
        } catch (JsonProcessingException e) {
            log.info(String.valueOf(e));
            e.printStackTrace();
        }

        return authorization;

    }

    @Override
    public Map<String, Object> getKakaoUserInfo(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(kakaoUserInfoUri, request, Object.class);

            // 값 리턴
            return objectMapper.convertValue(response.getBody(), Map.class);
        } catch (RestClientException ex) {
            log.info(String.valueOf(ex));
            ex.printStackTrace();
            throw new Exception(ex);
        }
    }
}
