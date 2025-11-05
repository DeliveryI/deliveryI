package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.menu.application.service.MenuQueryService;
import com.sparta.deliveryi.menu.domain.Menu;
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
     * CUSTOMER : HIDING 제외 + 삭제되지 않은 메뉴
     * OWNER : 자신의 가게면 전체, 타 가게면 HIDING 제외
     * MANAGER : 모든 메뉴 (삭제 제외)
     * MASTER : 모든 메뉴 (삭제 포함)
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
            @RequestParam(required = false) String currentStoreId
    ) {
        Page<Menu> menus = menuQueryService.getMenusByStore(
                storeId,
                currentStoreId,
                role,
                menuName,
                page,
                size,
                sortBy,
                direction
        );

        Page<MenuQueryResponse> response = menus.map(MenuQueryResponse::from);

        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }


    @GetMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuQueryResponse>> getMenu(@PathVariable Long menuId) {
        Menu menu = menuQueryService.getMenu(menuId);
        MenuQueryResponse response = MenuQueryResponse.from(menu);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }
}
