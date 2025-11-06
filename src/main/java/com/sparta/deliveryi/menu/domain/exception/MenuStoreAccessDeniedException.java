package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class MenuStoreAccessDeniedException extends AbstractException {
    public MenuStoreAccessDeniedException() {
        super(MenuMessageCode.MENU_STORE_ACCESS_DENIED);
    }
}
