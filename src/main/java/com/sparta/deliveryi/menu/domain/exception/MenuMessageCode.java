package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.MessageCode;
import org.springframework.http.HttpStatus;

public enum MenuMessageCode implements MessageCode {
    MENU_PRICE_INVALID("MENU.PRICE_INVALID", HttpStatus.BAD_REQUEST),
    MENU_CREATED_BY_EMPTY("MENU.CREATED_BY_EMPTY", HttpStatus.BAD_REQUEST),
    MENU_UPDATED_BY_EMPTY("MENU.UPDATED_BY_EMPTY", HttpStatus.BAD_REQUEST),
    MENU_NOT_FOUND("MENU.NOT_FOUND", HttpStatus.NOT_FOUND),
    MENU_DELETED("MENU.DELETED", HttpStatus.BAD_REQUEST),
    MENU_DELETE_FORBIDDEN("MENU.DELETE_FORBIDDEN", HttpStatus.FORBIDDEN),
    MENU_STORE_ACCESS_DENIED("MENU.STORE_ACCESS_DENIED", HttpStatus.FORBIDDEN);

    private final String code;
    private final HttpStatus status;

    MenuMessageCode(String code, HttpStatus status) {
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
