package com.sparta.deliveryi.order.domain.event;

import com.sparta.deliveryi.order.domain.OrderId;

import java.util.UUID;

public record OrderCancelEvent(OrderId orderId, int amount, UUID requestId) {
}
