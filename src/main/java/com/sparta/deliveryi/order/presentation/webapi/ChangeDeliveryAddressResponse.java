package com.sparta.deliveryi.order.presentation.webapi;

import com.sparta.deliveryi.order.domain.Order;

import java.util.UUID;

public record ChangeDeliveryAddressResponse(
        UUID orderId,
        String deliveryAddress
) {
    public static ChangeDeliveryAddressResponse from(Order order) {
        return new ChangeDeliveryAddressResponse(order.getId().toUuid(), order.getDeliveryAddress());
    }
}