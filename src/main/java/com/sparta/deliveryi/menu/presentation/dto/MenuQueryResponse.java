package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;

import java.util.UUID;

public record MenuQueryResponse(
        Long menuId,
        UUID storeId,
        String menuName,
        int menuPrice,
        String menuDescription,
        MenuStatus menuStatus
) {
    public static MenuQueryResponse from(Menu menu) {
        return new MenuQueryResponse(
                menu.getMenuId(),
                menu.getStoreId(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getMenuDescription(),
                menu.getMenuStatus()
        );
    }
}
