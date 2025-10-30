package com.sparta.deliveryi.store.domain;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreMessageCode implements MessageCode {
    STATUS_IS_NOT_READY("STORE.STATUS_IS_NOT_READY", HttpStatus.BAD_REQUEST),
    STATUS_IS_NOT_OPEN("STORE.STATUS_IS_NOT_OPEN", HttpStatus.BAD_REQUEST);

    private final String code;
    private final HttpStatus status;
}
