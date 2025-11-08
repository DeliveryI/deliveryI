package com.sparta.deliveryi.transaction.domain;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TransactionMessageCode implements MessageCode {
    UPDATE_NOT_ALLOWED("TRANSACTION.UPDATE_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED),
    DELETE_NOT_ALLOWED("TRANSACTION.DELETE_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED),
    ACCESS_FORBIDDEN("TRANSACTION.ACCESS_FORBIDDEN", HttpStatus.FORBIDDEN);

    private final String code;
    private final HttpStatus status;
}
