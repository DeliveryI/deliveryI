package com.sparta.deliveryi.payment.application.dto;

import java.util.List;

public record PaymentInfo(
    String paymentKey,
    String orderId,
    Integer totalAmount,
    String status,
    String requestedAt,
    String approvedAt,
    String lastTransactionKey,
    List<CancelInfo> cancels
    // String secret,
) {}
