package com.sparta.deliveryi.payment.application.dto;

import java.util.UUID;

public record PaymentConfirmCommand(
        String paymentKey,
        UUID orderId,
        int amount
) {}