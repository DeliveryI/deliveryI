package com.sparta.deliveryi.order.event;

import com.sparta.deliveryi.order.domain.OrderId;

import java.util.UUID;

public record OrderCreateEvent(OrderId orderId, int amount, UUID requestId) {
}
