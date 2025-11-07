package com.sparta.deliveryi.payment.application.dto;

import com.sparta.deliveryi.payment.domain.PaymentStatus;

public record PaymentSearchRequest(
        PaymentStatus status
) {}
