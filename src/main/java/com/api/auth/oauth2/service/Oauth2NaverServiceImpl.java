package com.api.auth.oauth2.service;

import com.api.auth.oauth2.domain.Authorization;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2NaverServiceImpl implements Oauth2NaverService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NaverOauth2ClinetId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NaverOauth2ClinetSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String frontendRedirectUrl;

    @Value("${spring.security.oauth2.client.provider.naver.token_uri}")
    private String NaverTokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NaverUserInfoUri;

    @Override
    public Authorization getAccessToken(String code, String state) {
        String grantType = "authorization_code";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", NaverOauth2ClinetId);
        params.add("client_secret", NaverOauth2ClinetSecret);
        params.add("state", state);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(NaverTokenUri, request, String.class);
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
    public Map<String, Object> getNaverUserInfo(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(NaverUserInfoUri, request, Object.class);

            // 값 리턴
            return objectMapper.convertValue(response.getBody(), Map.class);
        } catch (RestClientException ex) {
            log.info(String.valueOf(ex));
            ex.printStackTrace();
            throw new Exception(ex);
        }
    }
}
