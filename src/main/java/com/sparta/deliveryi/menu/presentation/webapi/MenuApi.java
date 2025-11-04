package com.sparta.deliveryi.menu.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.menu.application.service.MenuService;
import com.sparta.deliveryi.menu.presentation.dto.MenuRemoveResponse;
import com.sparta.deliveryi.menu.presentation.dto.MenuRequest;
import com.sparta.deliveryi.menu.presentation.dto.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/v1/menus")
@RequiredArgsConstructor
public class MenuApi {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @RequestParam UUID storeId,
            @RequestBody @Valid MenuRequest request
    ) {
        MenuResponse response = menuService.createMenu(storeId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.successWithDataOnly(response));
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable Long menuId,
            @RequestBody @Valid MenuRequest request
    ) {
        MenuResponse response = menuService.updateMenu(menuId, request);
        return ResponseEntity
                .ok(ApiResponse.successWithDataOnly(response));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuRemoveResponse>> deleteMenu(@PathVariable Long menuId) {
        MenuRemoveResponse response = menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResponse.successWithDataOnly(response));
    }

}
