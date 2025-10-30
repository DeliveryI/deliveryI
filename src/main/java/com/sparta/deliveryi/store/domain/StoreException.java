package com.sparta.deliveryi.store.domain;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class StoreException extends AbstractException {
    protected StoreException(MessageCode messageCode) {
        super(messageCode);
    }
}
