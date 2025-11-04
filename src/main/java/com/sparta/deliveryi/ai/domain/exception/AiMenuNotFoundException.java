package com.sparta.deliveryi.ai.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class AiMenuNotFoundException extends AbstractException {

    public AiMenuNotFoundException() {
        super(AiMessageCode.AI_MENU_NOT_FOUND);
    }
}
