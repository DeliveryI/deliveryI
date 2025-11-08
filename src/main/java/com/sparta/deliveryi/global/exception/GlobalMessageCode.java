package com.sparta.deliveryi.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalMessageCode implements MessageCode {
    UNKNOWN("GLOBAL.UNKNOWN", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_TOKEN_EXPIRED("GLOBAL.ACCESS_TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("GLOBAL.UNAUTHORIZED", HttpStatus.FORBIDDEN),
    ;

    private final String code;
    private final HttpStatus status;
}
