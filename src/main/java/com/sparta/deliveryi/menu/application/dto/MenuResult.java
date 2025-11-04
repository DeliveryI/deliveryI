package com.sparta.deliveryi.menu.application.dto;

import com.sparta.deliveryi.menu.domain.Menu;

public record MenuResult(
        Long menuId,
        String menuName,
        int menuPrice,
        String menuDescription,
        String menuStatus,
        boolean aiGenerated
) {
    public static MenuResult from(Menu menu, boolean aiGenerated) {
        return new MenuResult(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getMenuDescription(),
                menu.getMenuStatus().name(),
                aiGenerated
        );
    }
}
