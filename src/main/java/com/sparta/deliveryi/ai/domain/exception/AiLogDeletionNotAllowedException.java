package com.sparta.deliveryi.ai.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class AiLogDeletionNotAllowedException extends AbstractException {
    public AiLogDeletionNotAllowedException() {
        super(AiMessageCode.AI_LOG_DELETION_NOT_ALLOWED);
    }
}
