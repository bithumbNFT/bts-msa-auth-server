package com.api.auth.connectdb;

import com.api.auth.oauth2.domain.OAuth2Attribute;
import com.api.auth.user.domain.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface DbConnectionClient {
    User userSaveOrUpdateRest(@RequestBody OAuth2Attribute oAuth2Attribute);
}
