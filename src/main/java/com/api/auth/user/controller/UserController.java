package com.api.auth.user.controller;

import com.api.auth.connectdb.DbConnectService;
import com.api.auth.oauth2.domain.OAuth2Attribute;
import com.api.auth.user.domain.LoginDto;
import com.api.auth.user.domain.User;
import com.api.auth.user.domain.UserDto;
import com.api.auth.user.service.UserSerivceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserSerivceImpl userService;
    private final ModelMapper modelMapper;
    private final DbConnectService dbConnectService;

    @GetMapping("/hello")
    public String index() {
        log.info("test");
        return "hello-auth";
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> newRefreshToken(HttpServletRequest request) {

        String expiredAccessToken = request.getHeader("token");
        String refreshToken = request.getHeader("refresh");

        Object newToken = userService.refreshService(expiredAccessToken, refreshToken);

        return ResponseEntity.ok(modelMapper.map(newToken, Object.class));

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) throws Exception {
        UserDto info = userService.oauth2Authorization(loginDto.getCode(), loginDto.getState(), loginDto.getSocial());
        return ResponseEntity.ok(modelMapper.map(info, Object.class));
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, @RequestBody String social) {
        String token = request.getHeader("token");
        String refreshToken = request.getHeader("refresh");
        userService.logout(token, refreshToken, social);
    }


}
