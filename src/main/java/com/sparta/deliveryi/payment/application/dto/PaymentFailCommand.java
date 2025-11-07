package com.sparta.deliveryi.payment.application.dto;

import java.util.UUID;

public record PaymentFailCommand (
        UUID orderId,
        String code,
        String message
) {}
