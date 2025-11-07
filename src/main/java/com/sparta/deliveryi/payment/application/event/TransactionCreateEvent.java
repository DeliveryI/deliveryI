package com.sparta.deliveryi.payment.application.event;

import com.sparta.deliveryi.transaction.domain.TransactionResponse;
import com.sparta.deliveryi.transaction.domain.TransactionStatus;
import com.sparta.deliveryi.transaction.domain.TransactionType;

import java.util.UUID;

public record TransactionCreateEvent(
        Long paymentId,
        int amount,
        TransactionType type,
        TransactionStatus status,
        TransactionResponse response,
        UUID userId
) {}