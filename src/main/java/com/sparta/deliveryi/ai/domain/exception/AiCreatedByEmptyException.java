package com.sparta.deliveryi.ai.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class AiCreatedByEmptyException extends AbstractException {
    public AiCreatedByEmptyException() {
        super(AiMessageCode.AI_CREATED_BY_EMPTY);
    }
}