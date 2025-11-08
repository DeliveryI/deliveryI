package com.sparta.deliveryi.payment.domain;

import com.sparta.deliveryi.payment.application.dto.PaymentSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepositoryCustom {
    Page<Payment> search(PaymentSearchRequest search, Pageable pageable);
}
