package com.sparta.deliveryi.payment.domain.service;

import com.sparta.deliveryi.payment.domain.Payment;

import java.util.UUID;

public interface PaymentCreate {
    Payment register(UUID orderId, int amount, String createdBy);
}
