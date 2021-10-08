package com.api.auth.user.service;

import com.api.auth.config.GWErrorResponse;
import com.api.auth.connectdb.DbConnectService;
import com.api.auth.oauth2.domain.Authorization;
import com.api.auth.oauth2.domain.OAuth2Attribute;
import com.api.auth.oauth2.domain.SecurityToken;
import com.api.auth.oauth2.service.Oatuh2KakaoServiceimpl;
import com.api.auth.oauth2.service.Oauth2NaverServiceImpl;
import com.api.auth.oauth2.service.SecurityTokenService;
import com.api.auth.user.domain.Role;
import com.api.auth.user.domain.User;
import com.api.auth.user.domain.UserDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSerivceImpl implements UserService {
    private final SecurityTokenService securityTokenService;
    private final RedisServiceImpl redisServiceImpl;
    private final Oatuh2KakaoServiceimpl oatuh2KakaoServiceimpl;
    private final Oauth2NaverServiceImpl oauth2NaverServiceimpl;
    private final DbConnectService dbConnectService;

    @Override
    public Object refreshService(String expiredAccessToken, String refreshToken) {
        String email = null;

        try {
            email = securityTokenService.getEmail(expiredAccessToken);
        } catch (ExpiredJwtException e) {
            email = e.getClaims().getSubject();
            log.info("email from expired access token: " + email);
        }

        if (email == null) throw new IllegalArgumentException();
        String refreshTokenFromRedis = redisServiceImpl.findByEmail(email);
        if (!refreshToken.equals(refreshTokenFromRedis)) {
            log.info("\"refresh is not equal\"");
            return GWErrorResponse.defaultBuild("refresh is not equal", 58);
        }
        if (securityTokenService.checkExpiredRefreshToken(refreshToken)) {
            log.info("refresh token is expried go to loginpage");

            return GWErrorResponse.defaultBuild("refresh token is expried go to loginpage", 58);
        }

        SecurityToken newToken = securityTokenService.generateToken(email, Role.USER.name());
        return newToken;
    }

    @Override
    public UserDto oauth2Authorization(String code, String state, String type) throws Exception {
        Map<String, Object> userInfo = null;
        if (type.equals("kakao")) {
            Authorization authorization = oatuh2KakaoServiceimpl.getAccessToken(code);
            userInfo = oatuh2KakaoServiceimpl.getKakaoUserInfo(authorization.getAccess_token());
        } else {
            Authorization authorization = oauth2NaverServiceimpl.getAccessToken(code, state);
            userInfo = oauth2NaverServiceimpl.getNaverUserInfo(authorization.getAccess_token());
        }
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(type, userInfo);
        User user = dbConnectService.userSaveOrUpdateRest(oAuth2Attribute);
        log.info("save user info");

        SecurityToken securityToken = securityTokenService.generateToken(user.getEmail(), Role.USER.name());

        log.info("save our jwt token");
        redisServiceImpl.saveToken(user.getEmail() + "_" + type, securityToken.getRefreshToken());
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .picture(user.getPicture())
                .coinWallet(user.getCoinWallet())
                .token(securityToken.getToken())
                .refreshToken(securityToken.getRefreshToken())
                .build();
        return userDto;
    }

    @Override
    public void logout(String token, String refreshToken, String type) {
        log.info("delete refresh token");
        String email = securityTokenService.getEmail(token);
        redisServiceImpl.deleteToken(email + "_" + type);
    }
}
