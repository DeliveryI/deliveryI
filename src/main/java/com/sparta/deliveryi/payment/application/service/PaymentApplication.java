package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.payment.application.dto.*;
import com.sparta.deliveryi.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentApplication {
    Payment request(UUID userId, UUID orderId, int amount);
    PaymentResponse confirm(UUID userId, PaymentConfirmCommand command);
    void fail(UUID userId, PaymentFailCommand command);
    void refund(UUID userId, PaymentRefundCommand command);

    Payment getPaymentByOrderId(UUID userId, UUID orderId);
    Page<Payment> searchPayments(UUID userId, PaymentSearchRequest search, Pageable pageable);
}
