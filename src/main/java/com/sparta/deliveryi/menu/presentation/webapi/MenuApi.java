package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.menu.application.dto.*;
import com.sparta.deliveryi.menu.application.service.MenuService;
import com.sparta.deliveryi.menu.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "메뉴 Command API", description = "메뉴 등록, 수정, 삭제, 상태 변경 기능 제공")
@RestController
@RequestMapping("/v1/menus")
@RequiredArgsConstructor
public class MenuApi {

    private final MenuService menuService;

    @Operation(
            summary = "메뉴 등록",
            description = "메뉴를 등록할 수 있다. (Gemini API를 호출하여 메뉴 설명을 쉽게 작성할 수 있다.)"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Parameter(
                    name = "storeId",
                    description = "상점 ID (UUID)",
                    in = ParameterIn.QUERY,
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
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

    @Operation(
            summary = "메뉴 수정",
            description = "메뉴 정보를 수정할 수 있다."
    )
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @Parameter(name="menuId", description = "메뉴 ID", in = ParameterIn.PATH, required = true)
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

    @Operation(
            summary = "메뉴 삭제",
            description = "메뉴를 Soft Delete 방식으로 삭제할 수 있다."
    )
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @Parameter(name="id", description = "메뉴 ID", in = ParameterIn.PATH, required = true)
            @PathVariable Long menuId
    ) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(
            summary = "메뉴 상태 변경",
            description = "HIDING / FORSALE / SOLDOUT 중 하나로 메뉴 상태를 변경할 수 있다. 여러 메뉴를 동시에 변경할 수 있다."
    )
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
