package com.sparta.deliveryi.ai.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class AiPromptEmptyException extends AbstractException {
    public AiPromptEmptyException() {
        super(AiMessageCode.AI_PROMPT_EMPTY);
    }
}
