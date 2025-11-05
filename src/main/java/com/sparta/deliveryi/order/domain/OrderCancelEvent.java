package com.sparta.deliveryi.order.domain;

public record OrderCancelEvent(OrderId orderId, int totalPrice) {
}
