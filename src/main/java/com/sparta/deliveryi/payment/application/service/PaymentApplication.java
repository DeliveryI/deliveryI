package com.sparta.deliveryi.payment.application.service;

import com.sparta.deliveryi.payment.application.dto.PaymentConfirmCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentConfirmResponse;

import java.util.UUID;

public interface PaymentApplication {
    PaymentConfirmResponse confirm(UUID userId, PaymentConfirmCommand command);
}
