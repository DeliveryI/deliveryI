package com.sparta.deliveryi.transaction.application.service;

import com.sparta.deliveryi.transaction.application.dto.TransactionCreateCommand;
import com.sparta.deliveryi.transaction.application.dto.TransactionSearchRequest;
import com.sparta.deliveryi.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionApplication {
    Transaction register(TransactionCreateCommand command);
    Page<Transaction> search(UUID userId, TransactionSearchRequest search, Pageable pageable);
}
