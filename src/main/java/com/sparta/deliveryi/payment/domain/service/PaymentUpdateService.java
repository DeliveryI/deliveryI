package com.sparta.deliveryi.payment.domain.service;

import com.sparta.deliveryi.payment.domain.Payment;
import com.sparta.deliveryi.payment.domain.PaymentException;
import com.sparta.deliveryi.payment.domain.PaymentMessageCode;
import com.sparta.deliveryi.payment.domain.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentUpdateService implements PaymentUpdate {

    private final PaymentRepository repository;


    @Override
    public Payment approve(Long paymentId) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(PaymentMessageCode.PAYMENT_NOT_FOUND));

        payment.approve();

        return payment;
    }

    @Override
    public Payment failed(Long paymentId) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(PaymentMessageCode.PAYMENT_NOT_FOUND));

        payment.failed();

        return payment;
    }

    @Override
    public Payment refunded(Long paymentId) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(PaymentMessageCode.PAYMENT_NOT_FOUND));

        payment.refunded();

        return payment;
    }

    @Override
    public Payment updatePaymentKey(Long paymentId, String paymentKey) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentException(PaymentMessageCode.PAYMENT_NOT_FOUND));

        payment.updatePaymentKey(paymentKey);

        return payment;
    }
}
