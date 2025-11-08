package com.sparta.deliveryi.transaction.domain;

import com.sparta.deliveryi.transaction.application.dto.TransactionSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepositoryCustom {
    Page<Transaction> search(TransactionSearchRequest search, Pageable pageable);
}
