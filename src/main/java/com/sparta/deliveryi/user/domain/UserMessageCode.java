package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserMessageCode implements MessageCode {
    READ_FORBIDDEN("USER.READ_FORBIDDEN", HttpStatus.FORBIDDEN),
    UPDATE_FORBIDDEN("USER.UPDATE_FORBIDDEN", HttpStatus.FORBIDDEN),
    ACCESS_FORBIDDEN("USER.ACCESS_FORBIDDEN", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND("USER.USER_NOT_FOUND", HttpStatus.NOT_FOUND),
    CONFIRM_PASSWORD_MISMATCH("USER.CONFIRM_PASSWORD_MISMATCH", HttpStatus.BAD_REQUEST);

    private final String code;
    private final HttpStatus status;
}
