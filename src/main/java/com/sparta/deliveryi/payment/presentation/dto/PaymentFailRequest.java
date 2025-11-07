package com.sparta.deliveryi.payment.presentation.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentFailRequest (
    @NotNull UUID orderId,
    String code,
    String message
) {}
