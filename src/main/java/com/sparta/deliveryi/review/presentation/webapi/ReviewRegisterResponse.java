package com.sparta.deliveryi.review.presentation.webapi;

import com.sparta.deliveryi.review.domain.Review;

public record ReviewRegisterResponse(Long reviewId) {
    public static ReviewRegisterResponse from(Review review) {
        return new ReviewRegisterResponse(review.getId().value());
    }
}
