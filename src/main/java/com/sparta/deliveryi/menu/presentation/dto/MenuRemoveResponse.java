package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.domain.Menu;

public record MenuRemoveResponse(Long menuId) {
    public static MenuRemoveResponse from(Menu menu) {
        return new MenuRemoveResponse(menu.getMenuId());
    }
}
