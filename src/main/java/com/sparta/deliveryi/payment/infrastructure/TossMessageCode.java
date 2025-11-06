package com.sparta.deliveryi.payment.infrastructure;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum TossMessageCode implements MessageCode {
    INTERNAL_FAILED("TOSS.INTERNAL_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final HttpStatus status;
}
