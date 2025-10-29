package com.sparta.deliveryi.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalMessageCode implements MessageCode {
    UNKNOWN("GLOBAL.UNKNOWN", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String code;

    private final HttpStatus status;
}
