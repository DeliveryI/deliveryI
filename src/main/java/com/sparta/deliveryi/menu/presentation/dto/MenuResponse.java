package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.application.dto.MenuResult;

public record MenuResponse(
        Long menuId,
        String menuName,
        int menuPrice,
        String menuDescription,
        String menuStatus,
        boolean aiGenerated
) {
    public static MenuResponse from(MenuResult result) {
        return new MenuResponse(
                result.menuId(),
                result.menuName(),
                result.menuPrice(),
                result.menuDescription(),
                result.menuStatus(),
                result.aiGenerated()
        );
    }
}
