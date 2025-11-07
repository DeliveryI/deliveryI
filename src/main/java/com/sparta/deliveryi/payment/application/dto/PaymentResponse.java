package com.sparta.deliveryi.payment.application.dto;

import com.sparta.deliveryi.payment.infrastructure.dto.PaymentInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentResponse {
    int httpStatus;
    String code;
    String message;
    PaymentInfo payment;
}