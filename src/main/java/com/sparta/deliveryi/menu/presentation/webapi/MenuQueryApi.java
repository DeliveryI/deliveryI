package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.menu.application.service.MenuQueryService;
import com.sparta.deliveryi.menu.presentation.dto.MenuQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class MenuQueryApi {

    private final MenuQueryService menuQueryService;

    /**
     * [CUSTOMER / OWNER / MANAGER / MASTER] 공용 메뉴 목록 조회
     * - CUSTOMER : HIDING / deletedAt X
     * - OWNER : 자기 가게면 HIDING / 다른 가게면 HIDING X / deletedAt X
     * - MANAGER, MASTER : 모든 메뉴 조회 가능
     */
    @GetMapping("/{storeId}/menus")
    public ResponseEntity<ApiResponse<Page<MenuQueryResponse>>> getMenusByStore(
            @PathVariable String storeId,
            @RequestParam(required = false) String menuName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "CUSTOMER") String role,
            @RequestParam(required = false) String currentStoreId // 수정 예정
    ) {
        Page<MenuQueryResponse> menus = menuQueryService.getMenusByStore(
                storeId,
                currentStoreId,
                role,
                menuName,
                page,
                size,
                sortBy,
                direction
        );
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(menus));
    }

    // 메뉴 상세 조회
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuQueryResponse>> getMenu(@PathVariable Long menuId) {
        MenuQueryResponse response = menuQueryService.getMenu(menuId);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }
}
