package com.sparta.deliveryi.review.domain;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record ReviewSearchCondition(
        UUID storeId,
        Reviewer reviewer,
        String keyword,
        boolean includeDeleted,
        Pageable pageable
) {
    public static ReviewSearchCondition of(
            UUID storeId,
            Reviewer reviewer,
            String keyword,
            boolean includeDeleted,
            Pageable pageable
    ) {
        return new ReviewSearchCondition(storeId, reviewer, keyword, includeDeleted, pageable);
    }
}
