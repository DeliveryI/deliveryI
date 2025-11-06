package com.sparta.deliveryi.review.domain;

import java.util.UUID;

public record ReviewRegisterRequest(
        UUID storeId,
        UUID orderId,
        UUID reviewerId,
        int rating,
        String content
) {
}
