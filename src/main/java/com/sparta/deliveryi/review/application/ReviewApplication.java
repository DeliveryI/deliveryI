package com.sparta.deliveryi.review.application;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewApplication {
    Review register(ReviewRegisterRequest registerRequest, UUID requestId);

    Page<Review> search(UUID storeId, UUID reviewerId, String keyword, Pageable pageable, UUID requestId);

    Review update(Long reviewId, ReviewUpdateRequest updateRequest, UUID requestId);

    Review remove(Long reviewId, UUID requestId);
}
