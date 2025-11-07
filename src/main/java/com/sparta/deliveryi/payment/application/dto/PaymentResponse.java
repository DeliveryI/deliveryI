package com.sparta.deliveryi.payment.application.dto;

import com.sparta.deliveryi.payment.infrastructure.dto.TossResponse;

public record PaymentResponse (
    int httpStatus,
    String code,
    String message,
    TossResponse payment
){}