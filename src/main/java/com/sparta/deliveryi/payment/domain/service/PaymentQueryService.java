package com.sparta.deliveryi.payment.domain.service;

import com.sparta.deliveryi.payment.application.dto.PaymentSearchRequest;
import com.sparta.deliveryi.payment.domain.Payment;
import com.sparta.deliveryi.payment.domain.PaymentException;
import com.sparta.deliveryi.payment.domain.PaymentMessageCode;
import com.sparta.deliveryi.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentQueryService implements PaymentQuery {

    private final PaymentRepository repository;

    @Override
    public Payment getPaymentByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId).orElseThrow(() -> new PaymentException(PaymentMessageCode.PAYMENT_NOT_FOUND));
    }

    @Override
    public Page<Payment> searchPayments(PaymentSearchRequest search, Pageable pageable) {
        return repository.search(search, pageable);
    }
}
