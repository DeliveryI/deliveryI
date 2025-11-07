package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.payment.application.dto.PaymentResponse;

public interface TossQueryService {
    PaymentResponse getPaymentByOrderId(String orderId);
    PaymentResponse getPaymentByPaymentKey(String paymentKey);
}
