package com.sparta.deliveryi.transaction.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record TransactionResponse(
        String transactionKey,
        String code,
        String message
) {}
