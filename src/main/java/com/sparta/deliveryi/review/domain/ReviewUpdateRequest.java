package com.sparta.deliveryi.review.domain;

public record ReviewUpdateRequest(
        int rating,
        String content
) {
}
