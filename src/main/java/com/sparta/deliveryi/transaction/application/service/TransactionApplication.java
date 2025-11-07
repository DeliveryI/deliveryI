package com.sparta.deliveryi.transaction.application.service;

import com.sparta.deliveryi.transaction.application.dto.TransactionCreateCommand;
import com.sparta.deliveryi.transaction.domain.Transaction;

public interface TransactionApplication {
    Transaction register(TransactionCreateCommand command);
}
