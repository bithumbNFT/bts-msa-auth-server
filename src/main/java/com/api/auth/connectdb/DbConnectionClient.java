package com.api.auth.connectdb;

import com.api.auth.security.domain.OAuth2Attribute;
import com.api.auth.user.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user")
public interface DbConnectionClient {
    @PostMapping("/save-update")
    User saveOrUpdate(@RequestBody OAuth2Attribute oAuth2Attribute);
}
