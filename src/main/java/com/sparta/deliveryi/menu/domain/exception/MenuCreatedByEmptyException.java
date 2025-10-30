package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class MenuCreatedByEmptyException extends AbstractException {
    public MenuCreatedByEmptyException() {
        super(MenuMessageCode.MENU_CREATED_BY_EMPTY);
    }
}
