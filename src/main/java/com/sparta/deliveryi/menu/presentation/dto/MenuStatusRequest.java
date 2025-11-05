package com.sparta.deliveryi.menu.presentation.dto;

import com.sparta.deliveryi.menu.domain.MenuStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "메뉴 상태 변경 요청")
public record MenuStatusRequest(
        @Schema(description = "변경할 메뉴 상태 요청 목록", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "메뉴 상태 변경 요청 목록은 비어 있을 수 없습니다.")
        List<@Valid MenuStatusChangeItem> items
) {
    @Schema(description = "단일 메뉴 상태 변경 요청 항목")
    public record MenuStatusChangeItem(

            @Schema(description = "메뉴 ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "menuId는 필수입니다.")
            Long menuId,

            @Schema(description = "변경할 상태 (HIDING, FORSALE, SOLDOUT 중 하나)", example = "HIDING", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "status는 필수입니다.")
            MenuStatus status
    ) {}
}
