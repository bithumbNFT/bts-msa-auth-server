package com.api.auth.connectdb;

import com.api.auth.security.domain.OAuth2Attribute;
import com.api.auth.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class DbConnectService {
    private final RestTemplate restTemplate;
    private final DbConnectionClient dbConnectionClient;

    public User userSaveOrUpdateRest(OAuth2Attribute oAuth2Attribute) {
        URI uri = UriComponentsBuilder
                .fromUriString("http://db-server:8080")
                .path("/save_update")
                .encode()
                .build()
                .toUri();
        ResponseEntity<User> response = restTemplate.postForEntity(uri, oAuth2Attribute, User.class);
        User user = dbConnectionClient.saveOrUpdate(oAuth2Attribute);
        System.out.println(user.getEmail());
        System.out.println(user.getName());
        return null;
//        return response.getBody();
    }
}
