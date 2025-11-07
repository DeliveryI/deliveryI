package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.menu.application.dto.*;
import com.sparta.deliveryi.menu.application.service.MenuService;
import com.sparta.deliveryi.menu.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.success;
import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;

@Slf4j
@Tag(name = "메뉴 Command API", description = "메뉴 등록, 수정, 삭제, 상태 변경 기능 제공")
@RestController
@RequestMapping("/v1/menus")
@RequiredArgsConstructor
public class MenuApi {

    private final MenuService menuService;

    @Operation(
            summary = "메뉴 등록",
            description = "OWNER는 자신의 가게 메뉴만, MANAGER/MASTER는 전체 가게에 메뉴를 등록할 수 있다."
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @AuthenticationPrincipal Jwt jwt,
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
        log.info("storeId = {}", storeId.toString());
        UUID requestId = UUID.fromString(jwt.getSubject());

        MenuCommand command = new MenuCommand(
                request.menuName(),
                request.menuPrice(),
                request.menuStatus(),
                request.menuDescription(),
                request.aiGenerate(),
                request.prompt()
        );

        var result = menuService.createMenu(storeId, command, requestId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(successWithDataOnly(MenuResponse.from(result)));
    }

    @Operation(
            summary = "메뉴 수정",
            description = "OWNER는 자신의 가게 메뉴만, MANAGER/MASTER는 전체 메뉴 수정 가능할 수 있다."
    )
    @PreAuthorize("hasAnyRole('OWNER','MANAGER','MASTER')")
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "menuId", description = "메뉴 ID", in = ParameterIn.PATH, required = true)
            @PathVariable Long menuId,
            @Parameter(
                    name = "storeId",
                    description = "상점 ID (UUID)",
                    in = ParameterIn.QUERY,
                    required = true
            )
            @RequestParam UUID storeId,
            @RequestBody @Valid MenuRequest request
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        MenuCommand command = new MenuCommand(
                request.menuName(),
                request.menuPrice(),
                request.menuStatus(),
                request.menuDescription(),
                request.aiGenerate(),
                request.prompt()
        );

        var result = menuService.updateMenu(menuId, command, storeId, requestId);
        return ResponseEntity.ok(successWithDataOnly(MenuResponse.from(result)));
    }

    @Operation(
            summary = "메뉴 삭제",
            description = "Soft Delete 방식으로 메뉴를 삭제한다. OWNER는 자신의 가게 메뉴만 가능하다."
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "menuId", description = "메뉴 ID", in = ParameterIn.PATH, required = true)
            @PathVariable Long menuId,
            @Parameter(
                    name = "storeId",
                    description = "상점 ID (UUID)",
                    in = ParameterIn.QUERY,
                    required = true
            )
            @RequestParam UUID storeId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());
        menuService.deleteMenu(menuId, storeId, requestId);
        return ResponseEntity.ok(success());
    }

    @Operation(
            summary = "메뉴 상태 변경",
            description = "HIDING / FORSALE / SOLDOUT 중 하나로 메뉴 상태를 변경할 수 있다. OWNER는 자신의 가게 메뉴만 가능."
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/status")
    public ResponseEntity<ApiResponse<MenuStatusResponse>> changeMenuStatus(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(
                    name = "storeId",
                    description = "상점 ID (UUID)",
                    in = ParameterIn.QUERY,
                    required = true
            )
            @RequestParam UUID storeId,
            @RequestBody @Valid MenuStatusRequest request
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        List<MenuStatusChangeCommand> commands = request.items().stream()
                .map(item -> new MenuStatusChangeCommand(item.menuId(), item.status()))
                .toList();

        List<Long> updatedIds = menuService.changeMenuStatus(commands, storeId, requestId);
        return ResponseEntity.ok(successWithDataOnly(MenuStatusResponse.of(updatedIds)));
    }
}
