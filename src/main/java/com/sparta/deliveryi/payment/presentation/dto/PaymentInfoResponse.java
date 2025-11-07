package com.sparta.deliveryi.payment.presentation.dto;

import com.sparta.deliveryi.payment.domain.Payment;
import com.sparta.deliveryi.payment.domain.PaymentStatus;

import java.util.UUID;

public record PaymentInfoResponse (
        Long id,
        UUID orderId,
        String paymentKey,
        Integer totalPrice,
        PaymentStatus status
) {
    public static PaymentInfoResponse from(Payment payment) {
        return new PaymentInfoResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getPaymentKey(),
                payment.getTotalPrice(),
                payment.getStatus()
        );
    }
}
