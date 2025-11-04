package com.sparta.deliveryi.menu.presentation.dto;

import java.util.List;

public record MenuStatusResponse(
        List<Long> updatedMenuIds
) {
    public static MenuStatusResponse of(List<Long> ids) {
        return new MenuStatusResponse(ids);
    }
}
