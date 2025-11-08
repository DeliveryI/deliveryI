package com.sparta.deliveryi.transaction.application.dto;

import com.sparta.deliveryi.transaction.domain.TransactionResponse;
import com.sparta.deliveryi.transaction.domain.TransactionStatus;
import com.sparta.deliveryi.transaction.domain.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionCreateCommand(
        @NotNull Long paymentId,
        @NotNull Integer amount,
        @NotNull TransactionType type,
        @NotNull TransactionStatus status,
        TransactionResponse response,
        @NotBlank String username
) {}
