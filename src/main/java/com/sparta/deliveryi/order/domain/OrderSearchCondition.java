package com.sparta.deliveryi.order.domain;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record OrderSearchCondition(
        UUID storeId,
        Orderer orderer,
        Pageable pageable
) {
    public static OrderSearchCondition of(
            UUID storeId,
            Orderer orderer,
            Pageable pageable
    ) {
        return new OrderSearchCondition(storeId, orderer, pageable);
    }
}
