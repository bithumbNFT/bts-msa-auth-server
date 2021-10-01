package com.api.auth.user.service;

import com.api.auth.user.domain.UserDto;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    Object refreshService(String token, String refreshToken);

    UserDto oauth2Authorization(String code, String state, String type) throws Exception;

    void logout(String token, String refreshToken, String type);
}
