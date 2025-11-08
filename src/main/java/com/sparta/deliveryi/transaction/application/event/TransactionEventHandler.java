package com.sparta.deliveryi.transaction.application.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.payment.application.event.TransactionCreateEvent;
import com.sparta.deliveryi.transaction.application.dto.TransactionCreateCommand;
import com.sparta.deliveryi.transaction.application.service.TransactionApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class TransactionEventHandler {

    private final ObjectMapper mapper;
    private final TransactionApplication transactionApplication;

    @Async
    @TransactionalEventListener(TransactionCreateEvent.class)
    public void handleTransactionCreateEvent(TransactionCreateEvent event) {
        TransactionCreateCommand command = mapper.convertValue(event, TransactionCreateCommand.class);
        transactionApplication.register(command);
    }
}
