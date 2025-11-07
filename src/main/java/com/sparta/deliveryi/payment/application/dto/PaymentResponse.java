package com.sparta.deliveryi.payment.application.dto;

import com.sparta.deliveryi.payment.infrastructure.dto.PaymentInfo;

public record PaymentResponse (
    int httpStatus,
    String code,
    String message,
    PaymentInfo payment
){}