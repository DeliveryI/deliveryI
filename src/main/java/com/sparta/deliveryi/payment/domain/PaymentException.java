package com.sparta.deliveryi.payment.domain;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class PaymentException extends AbstractException {
    public PaymentException(MessageCode messageCode) {
        super(messageCode);
    }

    public PaymentException(MessageCode messageCode, String message) {
        super(messageCode, message);
    }

    public PaymentException(MessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }

    public PaymentException(MessageCode messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
    }
}
