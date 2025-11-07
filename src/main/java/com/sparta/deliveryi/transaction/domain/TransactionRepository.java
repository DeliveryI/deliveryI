package com.sparta.deliveryi.transaction.domain;

import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface TransactionRepository extends Repository<Transaction, UUID> {
    Transaction save(Transaction transaction);
}
