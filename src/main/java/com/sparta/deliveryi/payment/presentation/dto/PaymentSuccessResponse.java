package com.sparta.deliveryi.payment.presentation.dto;

import com.sparta.deliveryi.payment.application.dto.PaymentResponse;

public record PaymentSuccessResponse(
        String paymentKey,
        String orderId,
        int totalAmount,
        String requestedAt,
        String approvedAt
) {
    public static PaymentSuccessResponse from(PaymentResponse response) {
        return new PaymentSuccessResponse(
                response.payment().paymentKey(),
                response.payment().orderId(),
                response.payment().totalAmount(),
                response.payment().requestedAt(),
                response.payment().approvedAt()
        );
    }
}
