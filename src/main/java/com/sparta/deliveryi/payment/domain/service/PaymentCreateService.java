package com.sparta.deliveryi.payment.domain.service;

import com.sparta.deliveryi.payment.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentCreateService implements PaymentCreate {
    
    private final PaymentRepository repository;

    @Override
    public Payment register(UUID orderId, int amount, String createdBy) {
        // 중복 결제 방지
        repository.findByOrderId(orderId)
                .ifPresent(payment -> {
                    if (payment.getStatus() == PaymentStatus.PENDING) {
                        throw new PaymentException(PaymentMessageCode.DUPLICATE_PENDING_PAYMENT, orderId);
                    }
                    throw new PaymentException(PaymentMessageCode.INVALID_STATUS_CHANGE, orderId);
                });

        // 결제 생성
        Payment payment = Payment.create(orderId, amount, createdBy);
        
        return repository.save(payment);
    }
}
