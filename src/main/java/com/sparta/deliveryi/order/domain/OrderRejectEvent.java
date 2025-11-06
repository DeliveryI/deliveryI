package com.sparta.deliveryi.order.domain;

public record OrderRejectEvent(Long orderId, int totalPrice) {
}
