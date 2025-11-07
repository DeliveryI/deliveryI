package com.sparta.deliveryi.payment.infrastructure.dto;

import com.sparta.deliveryi.payment.application.dto.PaymentCancel;

import java.util.List;

public record PaymentInfo(
    String paymentKey,
    String orderId,
    Integer totalAmount,
    String status,
    String requestedAt,
    String approvedAt,
    String lastTransactionKey,
    List<PaymentCancel> cancels
    // String secret,
) {}
