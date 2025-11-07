package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record MenuDetailResponse(
        Long menuId,
        UUID storeId,
        String menuName,
        int menuPrice,
        String menuDescription,
        MenuStatus menuStatus,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        String deletedBy,
        LocalDateTime deletedAt
) {
    public static MenuDetailResponse from(Menu menu, boolean includeAudit) {
        if (includeAudit) {
            return new MenuDetailResponse(
                    menu.getMenuId(),
                    menu.getStoreId(),
                    menu.getMenuName(),
                    menu.getMenuPrice(),
                    menu.getMenuDescription(),
                    menu.getMenuStatus(),
                    menu.getCreatedBy(),
                    menu.getCreatedAt(),
                    menu.getUpdatedBy(),
                    menu.getUpdatedAt(),
                    menu.getDeletedBy(),
                    menu.getDeletedAt()
            );
        }

        // CUSTOMER/OWNER는 감사 정보 제외
        return new MenuDetailResponse(
                menu.getMenuId(),
                menu.getStoreId(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getMenuDescription(),
                menu.getMenuStatus(),
                null, null, null, null, null, null
        );
    }
}
