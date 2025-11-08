package com.sparta.deliveryi.review.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.review.application.ReviewApplication;
import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@Tag(name = "리뷰 API", description = "리뷰 등록, 조회, 수정, 삭제 관련 API 제공")
@RestController
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewApplication reviewApplication;

    @Operation(summary = "리뷰 등록", description = "가게에 대한 리뷰를 등록합니다.")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/reviews")
    public ResponseEntity<ApiResponse<ReviewRegisterResponse>> register(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid ReviewRegisterRequest registerRequest
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Review review = reviewApplication.register(registerRequest, requestId);

        return ok(successWithDataOnly(ReviewRegisterResponse.from(review)));
    }

    @Operation(summary = "리뷰 목록 조회", description = "가게 및 작성자 기준으로 검색하거나 키워드로 필터링합니다.")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @GetMapping("/v1/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewSearchResponse>>> search(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "스토어 ID", required = true)
            @RequestParam UUID storeId,
            @Parameter(description = "리뷰 작성자 ID", required = true)
            @RequestParam UUID reviewerId,
            @Parameter(description = "리뷰 내용 검색 키워드")
            @RequestParam String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Page<Review> reviews = reviewApplication.search(storeId, reviewerId, keyword, pageable, requestId);

        Page<ReviewSearchResponse> responses = reviews.map(ReviewSearchResponse::from);

        return ok(successWithDataOnly(responses));
    }

    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다.")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PutMapping("/v1/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewUpdateResponse>> update(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest updateRequest
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Review order = reviewApplication.update(reviewId, updateRequest, requestId);

        return ok(successWithDataOnly(ReviewUpdateResponse.from(order)));
    }

    @Operation(summary = "리뷰 삭제", description = "사용자가 작성한 리뷰를 삭제합니다.")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @DeleteMapping("/v1/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewRemoveResponse>> remove(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Review review = reviewApplication.remove(reviewId, requestId);

        return ok(successWithDataOnly(ReviewRemoveResponse.from(review)));
    }

}