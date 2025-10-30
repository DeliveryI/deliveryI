package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class MenuUpdatedByEmptyException extends AbstractException {
    public MenuUpdatedByEmptyException() {
        super(MenuMessageCode.MENU_UPDATED_BY_EMPTY);
    }
}