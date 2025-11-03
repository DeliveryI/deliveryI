package com.sparta.deliveryi.store.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.store.application.service.StoreApplication;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.success;
import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class StoreApi {

    private final StoreRegister storeRegister;

    private final StoreApplication storeApplication;

    @PostMapping("/v1/stores")
    public ResponseEntity<ApiResponse<StoreRegisterResponse>> register(@RequestBody @Valid StoreRegisterRequest registerRequest) {
        Store store = storeRegister.register(registerRequest);

        return ok(successWithDataOnly(StoreRegisterResponse.from(store)));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @DeleteMapping("/v1/stores/{storeId}")
    public ResponseEntity<ApiResponse<Void>> remove(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID storeId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        storeApplication.remove(storeId, requestId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptRegister(@PathVariable UUID storeId) {
        storeRegister.acceptRegisterRequest(storeId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectRegister(@PathVariable UUID storeId) {
        storeRegister.rejectRegisterRequest(storeId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/open")
    public ResponseEntity<ApiResponse<Void>> open(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID storeId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        storeApplication.open(storeId, requestId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/close")
    public ResponseEntity<ApiResponse<Void>> close(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID storeId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        storeApplication.open(storeId, requestId);

        return ok(success());
    }

}
