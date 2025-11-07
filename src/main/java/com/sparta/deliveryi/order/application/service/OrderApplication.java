package com.sparta.deliveryi.order.application.service;

import com.sparta.deliveryi.order.domain.Order;

import java.util.UUID;

public interface OrderApplication {
    Order changeDeliveryAddress(UUID orderId, String deliveryAddress, UUID requestId);

    void accept(UUID orderId, UUID requestId);

    void reject(UUID orderId, UUID requestId);

    void completeCooking(UUID orderId, UUID requestId);

    void delivery(UUID orderId, UUID requestId);

    void complete(UUID orderId, UUID requestId);
}
