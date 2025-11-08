package com.sparta.deliveryi.payment.application.dto;

import java.util.UUID;

public record PaymentRefundCommand(
        String reason,
        UUID orderId,
        int amount
) {}