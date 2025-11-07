package com.sparta.deliveryi.payment.domain;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentMessageCode implements MessageCode {
    PAYMENT_AMOUNT_MISMATCH("PAYMENT.PAYMENT_AMOUNT_MISMATCH", HttpStatus.BAD_REQUEST),
    PAYMENT_KEY_ALREADY_ASSIGNED("PAYMENT.PAYMENT_KEY_ALREADY_ASSIGNED", HttpStatus.CONFLICT),
    ALREADY_EXIST_PAYMENT("PAYMENT.ALREADY_EXIST_PAYMENT", HttpStatus.CONFLICT),
    DUPLICATE_PENDING_PAYMENT("PAYMENT.DUPLICATE_PENDING_PAYMENT", HttpStatus.CONFLICT),
    INVALID_STATUS_CHANGE("PAYMENT.INVALID_STATUS_CHANGE", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND("PAYMENT.NOT_FOUND", HttpStatus.NOT_FOUND);

    private final String code;
    private final HttpStatus status;
}
