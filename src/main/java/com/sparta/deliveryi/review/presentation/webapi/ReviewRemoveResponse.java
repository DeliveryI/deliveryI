package com.sparta.deliveryi.review.presentation.webapi;

import com.sparta.deliveryi.review.domain.Review;

public record ReviewRemoveResponse(Long reviewId) {
    public static ReviewRemoveResponse from(Review review) {
        return new ReviewRemoveResponse(review.getId().value());
    }
}
