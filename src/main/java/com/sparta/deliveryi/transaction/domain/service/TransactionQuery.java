package com.sparta.deliveryi.transaction.domain.service;

import com.sparta.deliveryi.transaction.application.dto.TransactionSearchRequest;
import com.sparta.deliveryi.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionQuery {
    Page<Transaction> search(TransactionSearchRequest search, Pageable pageable);
}
