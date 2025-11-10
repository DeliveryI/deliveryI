package com.sparta.deliveryi.payment.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record PaymentSuccessRequest(
        @NotBlank String paymentKey,
        @NotNull UUID orderId,
        @Positive Integer amount
) {}
