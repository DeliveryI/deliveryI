package com.sparta.deliveryi.order.domain;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderMessageCode implements MessageCode {
    CANCEL_TIMEOUT("ORDER.CANCEL_TIMEOUT", HttpStatus.INTERNAL_SERVER_ERROR),
    CANNOT_CANCEL_STATUS("ORDER.CANNOT_CANCEL_STATUS", HttpStatus.INTERNAL_SERVER_ERROR),
    BEFORE_PAYMENT("ORDER.BEFORE_PAYMENT", HttpStatus.INTERNAL_SERVER_ERROR),
    PAYMENT_ALREADY_COMPLETED("ORDER.PAYMENT_ALREADY_COMPLETED", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_CANCELED("ORDER.ORDER_ALREADY_CANCELED", HttpStatus.BAD_REQUEST),
    NOT_ACCEPTED_STATUS("ORDER.NOT_ACCEPTED_STATUS", HttpStatus.BAD_REQUEST),
    NOT_READY_TO_SERVED_STATUS("ORDER.NOT_READY_TO_SERVED_STATUS", HttpStatus.BAD_REQUEST),
    NOT_DELIVERING_STATUS("ORDER.NOT_DELIVERING_STATUS", HttpStatus.BAD_REQUEST),
    DELIVERING_OR_COMPLETED_STATUS("ORDER.DELIVERING_OR_COMPLETED_STATUS", HttpStatus.BAD_REQUEST),
    INVALID_ADDRESS("ORDER.INVALID_ADDRESS", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final HttpStatus status;
}
