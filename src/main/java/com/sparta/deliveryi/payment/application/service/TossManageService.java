package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.payment.application.dto.CancelRequest;
import com.sparta.deliveryi.payment.application.dto.PaymentInfo;

public interface TossManageService {
    PaymentInfo getPaymentByOrderId(String orderId);
    PaymentInfo getPaymentByPaymentKey(String paymentKey);
    PaymentInfo confirm(String paymentKey, String orderId, int amount);
    PaymentInfo cancel(String paymentKey, CancelRequest request);
}
