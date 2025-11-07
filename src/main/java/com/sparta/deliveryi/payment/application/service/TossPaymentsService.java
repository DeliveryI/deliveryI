package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.payment.application.dto.PaymentCancelRequest;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;

public interface TossPaymentsService {
    PaymentResponse confirm(String paymentKey, String orderId, int amount);
    PaymentResponse cancel(String paymentKey, PaymentCancelRequest request);
}
