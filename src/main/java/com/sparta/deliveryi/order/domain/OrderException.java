package com.sparta.deliveryi.order.domain;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class OrderException extends AbstractException {
    protected OrderException(MessageCode messageCode) {
        super(messageCode);
    }
}
