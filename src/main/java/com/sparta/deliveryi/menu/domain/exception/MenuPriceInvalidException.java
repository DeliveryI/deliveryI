package com.sparta.deliveryi.menu.domain.exception;

import com.sparta.deliveryi.global.exception.AbstractException;

public class MenuPriceInvalidException extends AbstractException {
    public MenuPriceInvalidException() {
        super(MenuMessageCode.MENU_PRICE_INVALID);
    }
}