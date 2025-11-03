package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserMessageCode implements MessageCode {
    NOT_FOUND("USER.NOT_FOUND", HttpStatus.NOT_FOUND);

    private final String code;
    private final HttpStatus status;
}
