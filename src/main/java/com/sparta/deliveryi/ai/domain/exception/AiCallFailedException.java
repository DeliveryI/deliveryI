package com.sparta.deliveryi.ai.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class AiCallFailedException extends AbstractException {
    public AiCallFailedException() {
        super(AiMessageCode.AI_CALL_FAILED);
    }
}
