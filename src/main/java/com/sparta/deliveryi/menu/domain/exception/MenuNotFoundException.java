package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class MenuNotFoundException extends AbstractException {
    public MenuNotFoundException() {
        super(MenuMessageCode.MENU_NOT_FOUND);
    }
}
