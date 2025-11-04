package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class MenuDeleteForbiddenException extends AbstractException {
    public MenuDeleteForbiddenException() {
        super(MenuMessageCode.MENU_DELETE_FORBIDDEN);
    }
}
