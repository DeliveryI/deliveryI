package com.sparta.deliveryi.transaction.domain.service;

import com.sparta.deliveryi.transaction.application.dto.TransactionSearchRequest;
import com.sparta.deliveryi.transaction.domain.Transaction;
import com.sparta.deliveryi.transaction.domain.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionQueryService implements TransactionQuery {

    private final TransactionRepository repository;

    @Override
    public Page<Transaction> search(TransactionSearchRequest search, Pageable pageable) {
        return repository.search(search, pageable);
    }
}
