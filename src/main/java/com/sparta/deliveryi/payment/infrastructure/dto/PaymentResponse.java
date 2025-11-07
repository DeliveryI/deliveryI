package com.sparta.deliveryi.payment.infrastructure.dto;

public record PaymentResponse (
    int httpStatus,
    String code,
    String message,
    PaymentInfo payment
){}