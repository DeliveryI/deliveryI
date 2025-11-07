package com.sparta.deliveryi.transaction.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.transaction.application.dto.TransactionCreateCommand;
import com.sparta.deliveryi.transaction.domain.Transaction;
import com.sparta.deliveryi.transaction.domain.dto.TransactionCreateRequest;
import com.sparta.deliveryi.transaction.domain.service.TransactionCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class TransactionApplicationService implements TransactionApplication {

    private final ObjectMapper mapper;
    private final TransactionCreate transactionCreate;

    @Override
    public Transaction register(TransactionCreateCommand command) {
        TransactionCreateRequest request = mapper.convertValue(command, TransactionCreateRequest.class);
        return transactionCreate.create(request);
    }
}
