package com.sparta.deliveryi.payment.domain.service;

import com.sparta.deliveryi.payment.domain.Payment;

public interface PaymentUpdate {
    Payment approve(Long paymentId);
    Payment failed(Long paymentId);
    Payment refunded(Long paymentId);
    Payment updatePaymentKey(Long paymentId, String paymentKey);
}
