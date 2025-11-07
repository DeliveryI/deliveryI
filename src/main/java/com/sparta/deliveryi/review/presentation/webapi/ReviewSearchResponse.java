package com.sparta.deliveryi.review.presentation.webapi;

import com.sparta.deliveryi.review.domain.Review;

public record ReviewSearchResponse(
        Long reviewId,
        String content,
        String rating
) {
    public static ReviewSearchResponse from(Review review) {
        return new ReviewSearchResponse(
                review.getId().value(),
                review.getContent(),
                review.getRating().score()
        );
    }
}
