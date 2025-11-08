package com.sparta.deliveryi.transaction.presentation.dto;

import com.sparta.deliveryi.transaction.domain.Transaction;
import com.sparta.deliveryi.transaction.domain.TransactionResponse;
import com.sparta.deliveryi.transaction.domain.TransactionStatus;
import com.sparta.deliveryi.transaction.domain.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionInfoResponse(
        UUID id,
        Long paymentId,
        Integer amount,
        TransactionType type,
        TransactionStatus status,
        TransactionResponse response,
        LocalDateTime createdAt,
        String createBy

) {
    public static TransactionInfoResponse from(Transaction transaction) {
        return new TransactionInfoResponse(
                transaction.getId().toUuid(),
                transaction.getPaymentId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getResponse(),
                transaction.getCreatedAt(),
                transaction.getCreateBy()
        );
    }
}
