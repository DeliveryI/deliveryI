package com.sparta.deliveryi.order.domain;

public record OrderRejectEvent(OrderId orderId, int totalPrice) {
}
