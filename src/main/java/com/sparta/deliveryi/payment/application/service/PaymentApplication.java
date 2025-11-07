package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.payment.application.dto.PaymentConfirmCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;

import java.util.UUID;

public interface PaymentApplication {
    PaymentResponse confirm(UUID userId, PaymentConfirmCommand command);
}
