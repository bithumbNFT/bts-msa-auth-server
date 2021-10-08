package com.api.auth.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class GWErrorResponse {
    private String errorMessage;
    private int errorCode;
    private LocalDateTime localDateTime;
    private Map<String, Object> addtionInfo = new HashMap<>();

    public GWErrorResponse(String errorMessage, int errorCode, LocalDateTime localDateTime) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.localDateTime = localDateTime;
    }

    public static GWErrorResponse defaultBuild(String errorMessage, int errorCode) {
        log.info("error : {}  ", errorMessage);
        return new GWErrorResponse(errorMessage, errorCode, LocalDateTime.now());
    }
}
