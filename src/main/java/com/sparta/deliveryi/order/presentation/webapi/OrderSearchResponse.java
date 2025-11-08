package com.sparta.deliveryi.order.presentation.webapi;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderStatus;

import java.util.UUID;

public record OrderSearchResponse(
        UUID orderId,
        Integer totalPrice,
        String deliveryAddress,
        OrderStatus status

) {
    public static OrderSearchResponse from(Order order) {
        return new OrderSearchResponse(
                order.getId().toUuid(),
                order.getTotalPrice(),
                order.getDeliveryAddress(),
                order.getStatus()
        );
    }
}
