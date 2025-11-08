package com.sparta.deliveryi.transaction.domain;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class TransactionException extends AbstractException {
    public TransactionException(MessageCode messageCode) {
        super(messageCode);
    }

    public TransactionException(MessageCode messageCode, String message) {
        super(messageCode, message);
    }

    public TransactionException(MessageCode messageCode, Object... messageArguments) {
        super(messageCode, messageArguments);
    }

    public TransactionException(MessageCode messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
    }
}
