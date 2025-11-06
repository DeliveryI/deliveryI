package com.sparta.deliveryi.order.domain;

import java.util.UUID;

public record OrderRejectEvent(UUID orderId, int totalPrice) {
}
