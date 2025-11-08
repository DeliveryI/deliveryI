package com.sparta.deliveryi.payment.application.dto;

public record PaymentCancel(
        Integer cancelAmount,
        String cancelReason,
        String canceledAt,
        String transactionKey,
        String cancelStatus,
        String cancelRequestId
) {}
