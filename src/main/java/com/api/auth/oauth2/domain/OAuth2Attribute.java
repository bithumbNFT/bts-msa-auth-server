package com.api.auth.oauth2.domain;

import com.api.auth.user.domain.Role;
import com.api.auth.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private String attributeKey;
    private String email;
    private String name;
    private String picture;

    @Builder
    public OAuth2Attribute (String attributeKey,String email , String name,String picture){
        this.attributeKey = attributeKey;
        this.email = email;
        this.name = name;
        this.picture =picture;
    }

    public static OAuth2Attribute of(String registrationId, Map<String, Object> attributes) {
        //네이버 추가 해야함
        log.info("요청 :: " + registrationId);
        log.info("속성 :: " + attributes);
        if (registrationId.equals("naver")) {
            return ofNaver("naver", attributes);
        }
        return ofKakao("kakao", attributes);
    }

    private static OAuth2Attribute ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) kakaoProfile.get("profile_image_url"))
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofNaver(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2Attribute.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributeKey(attributeKey)
                .build();
    }

    public User toEntity() {
        return User.builder().social(attributeKey).name(name).email(email).picture(picture).role(Role.USER).build();
    }

}
