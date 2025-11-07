package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.payment.application.dto.PaymentConfirmCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentFailCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;
import com.sparta.deliveryi.payment.application.dto.PaymentSearchRequest;
import com.sparta.deliveryi.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentApplication {
    PaymentResponse confirm(UUID userId, PaymentConfirmCommand command);
    void fail(UUID userId, PaymentFailCommand command);

    Payment getPaymentByOrderId(UUID userId, UUID orderId);
    Page<Payment> searchPayments(UUID userId, PaymentSearchRequest search, Pageable pageable);
}
