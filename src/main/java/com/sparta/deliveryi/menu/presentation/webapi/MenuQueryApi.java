package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.menu.application.service.MenuQueryService;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.presentation.dto.MenuQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "메뉴 Query API", description = "메뉴 상세 조회, 권한별 목록 조회 기능 제공")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class MenuQueryApi {

    private final MenuQueryService menuQueryService;

    @Operation(
            summary = "메뉴 목록 조회",
            description = """
                   [CUSTOMER / OWNER / MANAGER / MASTER] 공용 메뉴 목록을 조회할 수 있다.
                    - CUSTOMER : HIDING 제외 + 삭제되지 않은 메뉴
                    - OWNER : 자신의 가게면 전체, 타 가게면 HIDING 제외
                    - MANAGER, MASTER : 모든 메뉴 (삭제 포함)
                   """
    )
    @GetMapping("/{storeId}/menus")
    public ResponseEntity<ApiResponse<Page<MenuQueryResponse>>> getMenusByStore(
            @Parameter(name = "storeId", description = "가게 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID storeId,

            @Parameter(name = "menuName", description = "메뉴 이름 (검색어)", example = "전복죽")
            @RequestParam(required = false) String menuName,

            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(name = "size", description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(name = "sortBy", description = "정렬 기준 필드명", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(name = "direction", description = "정렬 방향 (ASC, DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String direction,

            @Parameter(name = "role", description = "조회자 권한 (CUSTOMER, OWNER, MANAGER, MASTER)", example = "CUSTOMER")
            @RequestParam(defaultValue = "CUSTOMER") String role,

            @Parameter(name = "currentStoreId", description = "현재 로그인한 사용자의 가게 ID (OWNER 권한일 때만 필요)", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam(required = false) UUID currentStoreId
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

    @Operation(
            summary = "메뉴 상세 조회",
            description = "메뉴Id를 통해 메뉴를 상세 조회할 수 있다."
    )
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuQueryResponse>> getMenu(
            @Parameter(name = "menuId", description = "메뉴 ID", example = "101")
            @PathVariable Long menuId
    ) {
        Menu menu = menuQueryService.getMenu(menuId);
        MenuQueryResponse response = MenuQueryResponse.from(menu);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }
}
