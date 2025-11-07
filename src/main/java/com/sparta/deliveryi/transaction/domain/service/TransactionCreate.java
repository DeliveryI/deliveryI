package com.sparta.deliveryi.transaction.domain.service;

import com.sparta.deliveryi.transaction.domain.Transaction;
import com.sparta.deliveryi.transaction.domain.dto.TransactionCreateRequest;
import jakarta.validation.Valid;

public interface TransactionCreate {
    Transaction create(@Valid TransactionCreateRequest request);
}
