package com.sparta.deliveryi.ai.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class AiResponseEmptyException extends AbstractException {
    public AiResponseEmptyException() {
        super(AiMessageCode.AI_RESPONSE_EMPTY);
    }
}