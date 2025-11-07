package com.sparta.deliveryi.payment.application.event;

import java.util.UUID;

public record PaymentSuccessEvent (UUID orderId, UUID requestId) {}
