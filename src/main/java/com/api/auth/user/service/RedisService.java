package com.api.auth.user.service;

import org.springframework.stereotype.Component;

@Component
public interface RedisService {
    void saveToken(String email, String token);
    void deleteToken(String email);
    String findByEmail(String email);
}
