package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.menu.application.dto.*;
import com.sparta.deliveryi.menu.application.service.MenuService;
import com.sparta.deliveryi.menu.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/menus")
@RequiredArgsConstructor
public class MenuApi {

    private final MenuService menuService;

    /** 메뉴 등록 */
    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @RequestParam UUID storeId,
            @RequestBody @Valid MenuRequest request
    ) {
        MenuCommand command = new MenuCommand(
                request.menuName(),
                request.menuPrice(),
                request.menuStatus(),
                request.menuDescription(),
                request.aiGenerate(),
                request.prompt()
        );

        var result = menuService.createMenu(storeId, command);
        MenuResponse response = MenuResponse.from(result);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.successWithDataOnly(response));
    }

    /** 메뉴 수정 */
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable Long menuId,
            @RequestBody @Valid MenuRequest request
    ) {
        MenuCommand command = new MenuCommand(
                request.menuName(),
                request.menuPrice(),
                request.menuStatus(),
                request.menuDescription(),
                request.aiGenerate(),
                request.prompt()
        );

        var result = menuService.updateMenu(menuId, command);
        MenuResponse response = MenuResponse.from(result);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }

    /** 메뉴 삭제 */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /** 메뉴 상태 변경 */
    @PostMapping("/status")
    public ResponseEntity<ApiResponse<MenuStatusResponse>> changeMenuStatus(
            @RequestBody @Valid MenuStatusRequest request
    ) {
        String updatedBy = getCurrentUsername();

        List<MenuStatusChangeCommand> commands = request.items().stream()
                .map(item -> new MenuStatusChangeCommand(item.menuId(), item.status()))
                .toList();

        List<Long> updatedIds = menuService.changeMenuStatus(commands, updatedBy);
        MenuStatusResponse response = MenuStatusResponse.of(updatedIds);

        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }

    /** 현재 로그인한 사용자명 */
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName()
                : "system";
    }
}
