package com.sparta.deliveryi.order.presentation.webapi;

import com.sparta.deliveryi.order.domain.Order;

import java.util.UUID;

public record OrderCreateResponse(
        UUID orderId,
        int totalPrice
) {
    public static OrderCreateResponse from(Order order) {
        return new OrderCreateResponse(order.getId().toUuid(), order.getTotalPrice());
    }
}