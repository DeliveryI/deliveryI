package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class UserException extends AbstractException {
    public UserException(MessageCode messageCode) {
        super(messageCode);
    }
}
