package com.sparta.deliveryi.payment.application.event;

import java.util.UUID;

public record PaymentFailEvent (UUID orderId, UUID requestId) {}
