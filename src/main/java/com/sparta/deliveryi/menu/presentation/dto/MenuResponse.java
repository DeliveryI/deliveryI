package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;

public record MenuResponse(
    Long menuId,
    String menuName,
    int menuPrice,
    String menuDescription,
    MenuStatus menuStatus,
    boolean aiGenerated
) {
    public static MenuResponse from(Menu menu, boolean aiGenerated) {
        return new MenuResponse(
            menu.getMenuId(),
            menu.getMenuName(),
            menu.getMenuPrice(),
            menu.getMenuDescription(),
            menu.getMenuStatus(),
            aiGenerated
        );
    }
}
