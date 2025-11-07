package com.sparta.deliveryi.payment.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends Repository<Payment, Long> {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByOrderId(UUID orderId);
}
