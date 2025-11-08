package com.sparta.deliveryi.transaction.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.transaction.application.dto.TransactionCreateCommand;
import com.sparta.deliveryi.transaction.application.dto.TransactionSearchRequest;
import com.sparta.deliveryi.transaction.domain.Transaction;
import com.sparta.deliveryi.transaction.domain.TransactionException;
import com.sparta.deliveryi.transaction.domain.TransactionMessageCode;
import com.sparta.deliveryi.transaction.domain.dto.TransactionCreateRequest;
import com.sparta.deliveryi.transaction.domain.service.TransactionCreate;
import com.sparta.deliveryi.transaction.domain.service.TransactionQuery;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
class TransactionApplicationService implements TransactionApplication {

    private final ObjectMapper mapper;
    private final TransactionCreate transactionCreate;
    private final TransactionQuery transactionQuery;
    private final UserRolePolicy userRolePolicy;

    @Override
    public Transaction register(TransactionCreateCommand command) {
        TransactionCreateRequest request = mapper.convertValue(command, TransactionCreateRequest.class);
        return transactionCreate.create(request);
    }

    @Override
    public Page<Transaction> search(UUID userId, TransactionSearchRequest search, Pageable pageable) {
        if (!userRolePolicy.isAdmin(userId)) {
            throw new TransactionException(TransactionMessageCode.ACCESS_FORBIDDEN);
        }
        return transactionQuery.search(search, pageable);
    }
}
