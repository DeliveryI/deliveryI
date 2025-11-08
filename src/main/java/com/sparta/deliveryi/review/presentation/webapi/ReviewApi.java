package com.sparta.deliveryi.review.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.review.application.ReviewApplication;
import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
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

@RestController
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewApplication reviewApplication;

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

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @GetMapping("/v1/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewSearchResponse>>> search(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam UUID storeId,
            @RequestParam UUID reviewerId,
            @RequestParam String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Page<Review> reviews = reviewApplication.search(storeId, reviewerId, keyword, pageable, requestId);

        Page<ReviewSearchResponse> responses = reviews.map(ReviewSearchResponse::from);

        return ok(successWithDataOnly(responses));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PutMapping("/v1/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewUpdateResponse>> update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest updateRequest
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Review order = reviewApplication.update(reviewId, updateRequest, requestId);

        return ok(successWithDataOnly(ReviewUpdateResponse.from(order)));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @DeleteMapping("/v1/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewRemoveResponse>> remove(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        Review review = reviewApplication.remove(reviewId, requestId);

        return ok(successWithDataOnly(ReviewRemoveResponse.from(review)));
    }

}