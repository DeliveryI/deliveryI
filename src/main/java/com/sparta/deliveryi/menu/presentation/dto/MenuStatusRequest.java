package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.domain.MenuStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record MenuStatusRequest(
        @NotEmpty(message = "메뉴 상태 변경 요청 목록은 비어 있을 수 없습니다.")
        List<@Valid MenuStatusChangeItem> items
) {
    public record MenuStatusChangeItem(
            Long menuId,
            MenuStatus status
    ) {}
}
