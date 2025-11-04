package com.sparta.deliveryi.ai.domain.exception;

import com.sparta.deliveryi.global.exception.MessageCode;
import org.springframework.http.HttpStatus;

public enum AiMessageCode implements MessageCode {
    AI_PROMPT_EMPTY("AI_PROMPT_EMPTY", HttpStatus.BAD_REQUEST),
    AI_RESPONSE_EMPTY("AI_RESPONSE_EMPTY", HttpStatus.BAD_REQUEST),
    AI_CREATED_BY_EMPTY("AI_CREATED_BY_EMPTY", HttpStatus.BAD_REQUEST),
    AI_LOG_DELETION_NOT_ALLOWED("AI_LOG_DELETION_NOT_ALLOWED", HttpStatus.BAD_REQUEST),
    AI_LOG_MENU_NOT_FOUND("AI_LOG_MENU_NOT_FOUND", HttpStatus.NOT_FOUND),
    AI_CALL_FAILED("AI_CALL_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final HttpStatus status;

    AiMessageCode(String code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
