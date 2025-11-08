package com.sparta.deliveryi.transaction.domain.service;

import com.sparta.deliveryi.transaction.domain.Transaction;
import com.sparta.deliveryi.transaction.domain.TransactionRepository;
import com.sparta.deliveryi.transaction.domain.dto.TransactionCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class TransactionCreateService implements TransactionCreate {

    private final TransactionRepository repository;

    @Override
    public Transaction create(@Valid TransactionCreateRequest request) {
        Transaction transaction = Transaction.create(
                request.paymentId(),
                request.amount(),
                request.type(),
                request.status(),
                request.response(),
                request.username()
        );

        return repository.save(transaction);
    }
}
