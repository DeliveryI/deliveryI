package com.sparta.deliveryi.store.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.store.application.service.StoreApplication;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.success;
import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@Tag(name = "가게 API", description = "가게 등록, 수정, 삭제, 검색 및 상태 변경 기능 제공")
@RestController
@RequiredArgsConstructor
public class StoreApi {

    private final StoreRegister storeRegister;

    private final StoreApplication storeApplication;

    @Operation(
            summary = "가게 등록",
            description = "신규 가게를 등록합니다."
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/stores")
    public ResponseEntity<ApiResponse<StoreRegisterResponse>> register(@RequestBody @Valid StoreRegisterRequest registerRequest) {
        Store store = storeRegister.register(registerRequest);

        return ok(successWithDataOnly(StoreRegisterResponse.from(store)));
    }

    @Operation(
            summary = "가게 검색",
            description = "가게를 검색할 수 있습니다. keyword는 가게명, 가게 분류 또는 설명을 기준으로 검색됩니다."
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @GetMapping("/v1/reviews")
    public ResponseEntity<ApiResponse<Page<StoreSearchResponse>>> search(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "ownerId", description = "가게 주인 ID (UUID)", in = ParameterIn.QUERY, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam UUID ownerId,
            @Parameter(name = "keyword", description = "검색 키워드 (가게명, 설명 등)", in = ParameterIn.QUERY, example = "치킨")
            @RequestParam String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Page<Store> stores = storeApplication.search(ownerId, keyword, requestId, pageable);

        Page<StoreSearchResponse> responses = stores.map(StoreSearchResponse::from);

        return ok(successWithDataOnly(responses));
    }

    @Operation(
            summary = "가게 정보 수정",
            description = "OWNER, MANAGER, MASTER 권한 사용자는 가게 정보를 수정할 수 있습니다."
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PutMapping("/v1/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreUpdateInfoResponse>> updateInfo(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "storeId", description = "가게 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID storeId,
            @RequestBody @Valid StoreInfoUpdateRequest updateRequest) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Store store = storeApplication.updateInfo(storeId, updateRequest, requestId);

        return ok(successWithDataOnly(StoreUpdateInfoResponse.from(store)));
    }

    @Operation(
            summary = "가게 삭제",
            description = "OWNER, MANAGER, MASTER 권한 사용자는 가게를 삭제할 수 있습니다. (Soft Delete)"
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @DeleteMapping("/v1/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreRemoveResponse>> remove(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "storeId", description = "가게 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID storeId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Store store = storeApplication.remove(storeId, requestId);

        return ok(successWithDataOnly(StoreRemoveResponse.from(store)));
    }

    @Operation(
            summary = "가게 등록 승인",
            description = "MANAGER 또는 MASTER가 가게 등록 요청을 승인합니다."
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptRegister(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "storeId", description = "가게 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID storeId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        storeRegister.acceptRegisterRequest(storeId, requestId);

        return ok(success());
    }

    @Operation(
            summary = "가게 등록 거절",
            description = "MANAGER 또는 MASTER가 가게 등록 요청을 반려합니다."
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectRegister(
            @Parameter(name = "storeId", description = "가게 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID storeId
    ) {
        storeRegister.rejectRegisterRequest(storeId);

        return ok(success());
    }

    @Operation(
            summary = "가게 영업 시작",
            description = "OWNER, MANAGER, MASTER 권한 사용자가 영업 시작할 수 있습니다."
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/open")
    public ResponseEntity<ApiResponse<Void>> open(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID storeId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        storeApplication.open(storeId, requestId);

        return ok(success());
    }

    @Operation(
            summary = "가게 영업 종료",
            description = "OWNER, MANAGER, MASTER 권한 사용자가 영업 종료할 수 있습니다."
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/close")
    public ResponseEntity<ApiResponse<Void>> close(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "storeId", description = "가게 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID storeId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        storeApplication.open(storeId, requestId);

        return ok(success());
    }

    @Operation(
            summary = "가게 인수인계",
            description = "OWNER, MANAGER, MASTER 권한 사용자가 가게 소유권을 다른 OWNER에게 이전할 수 있습니다."
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/stores/{storeId}/transfer")
    public ResponseEntity<ApiResponse<Void>> transfer(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "storeId", description = "가게 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID storeId,
            @RequestBody @Valid StoreTransferRequest transferRequest) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        storeApplication.transfer(storeId, transferRequest.newOwnerId(), requestId);

        return ok(success());
    }

}
