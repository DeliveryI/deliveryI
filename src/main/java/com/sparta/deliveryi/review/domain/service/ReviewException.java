package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class ReviewException extends AbstractException {
    public ReviewException(MessageCode messageCode) {
        super(messageCode);
    }
}
