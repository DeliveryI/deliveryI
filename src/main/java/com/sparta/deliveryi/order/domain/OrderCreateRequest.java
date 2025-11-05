package com.sparta.deliveryi.order.domain;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderCreateRequest(
        @NotNull UUID storeId,
        @NotNull UUID ordererId,
        @NotNull List<OrderItem> orderItems,
        @NotNull String deliveryAddress
) {
}
