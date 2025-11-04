package com.sparta.deliveryi.user.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.user.application.dto.AdminUserResponse;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.application.service.UserQuery;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/users")
@Tag(name = "회원 API(관리자용)", description = "관리자용 API로, 회원 정보 조회 시 모든 항목을 확인할 수 있습니다.")
public class AdminUserApi {

    private final UserQuery userQuery;

    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @Operation(summary = "회원 목록 조회", description = "등록된 모든 회원 정보를 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<AdminUserResponse>>> search(
            @AuthenticationPrincipal Jwt jwt,
            UserSearchRequest searchRequest,
            @PageableDefault Pageable pageable
    ) {
        Page<AdminUserResponse> response = userQuery.searchUsersForAdminById(UUID.fromString(jwt.getSubject()), searchRequest, pageable);

        return ok(successWithDataOnly(response));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @Operation(summary = "특정 회원 정보 조회(관리자용)", description = "UserId로 다른 회원의 모든 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getMyInfo(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID userId
    ) {
        AdminUserResponse response = userQuery.getUserForAdminById(UUID.fromString(jwt.getSubject()), userId);

        return ok(successWithDataOnly(response));
    }
}
