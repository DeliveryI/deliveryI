package com.sparta.deliveryi.payment.domain.service;

import com.sparta.deliveryi.payment.application.dto.PaymentSearchRequest;
import com.sparta.deliveryi.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentQuery {
    Payment getPaymentByOrderId(UUID orderId);
    Page<Payment> getPayments(PaymentSearchRequest search, Pageable pageable);
}
