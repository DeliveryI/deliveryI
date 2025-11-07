package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewMessageCode implements MessageCode {
    UPDATE_PERMISSION_DENIED("REVIEW.UPDATE_PERMISSION_DENIED", HttpStatus.FORBIDDEN),
    ;

    private final String code;
    private final HttpStatus status;
}
