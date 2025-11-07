package com.sparta.deliveryi.order.event;

import java.util.UUID;

public record OrderRejectEvent(UUID orderId, String reason, int amount, UUID requestId) {
}
