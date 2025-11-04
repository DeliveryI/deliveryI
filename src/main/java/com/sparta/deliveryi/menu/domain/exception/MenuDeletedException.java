package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class MenuDeletedException extends AbstractException {
    public MenuDeletedException() {
        super(MenuMessageCode.MENU_DELETED);
    }
}
