package com.sparta.deliveryi.review.presentation.webapi;

import com.sparta.deliveryi.review.domain.Review;

public record ReviewUpdateResponse(Long reviewId) {
    public static ReviewUpdateResponse from(Review review) {
        return new ReviewUpdateResponse(review.getId().value());
    }
}
