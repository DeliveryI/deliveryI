package com.sparta.deliveryi.transaction.application.dto;

import com.sparta.deliveryi.transaction.domain.TransactionStatus;
import com.sparta.deliveryi.transaction.domain.TransactionType;

import java.util.UUID;

public record TransactionSearchRequest (
        UUID orderId, TransactionType type, TransactionStatus status, String username
) {}
