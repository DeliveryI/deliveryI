package com.sparta.deliveryi.payment.infrastructure.dto;

public record ErrorResponse (
        String code,
        String message
) {}
