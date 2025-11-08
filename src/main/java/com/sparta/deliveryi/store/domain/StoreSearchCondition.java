package com.sparta.deliveryi.store.domain;

import org.springframework.data.domain.Pageable;

public record StoreSearchCondition(
        Owner owner,
        String keyword,
        boolean includeDeleted,
        Pageable pageable
) {
    public static StoreSearchCondition of(
            Owner owner,
            String keyword,
            boolean includeDeleted,
            Pageable pageable
    ) {
        return new StoreSearchCondition(owner, keyword, includeDeleted, pageable);
    }
}
