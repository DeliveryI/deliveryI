package com.sparta.deliveryi.order.domain.event;

import java.util.UUID;

public record OrderRejectEvent(UUID orderId, String reason, int amount, UUID requestId) {
}
