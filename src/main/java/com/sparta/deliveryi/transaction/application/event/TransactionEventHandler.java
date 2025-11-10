package com.sparta.deliveryi.transaction.application.event;

import com.sparta.deliveryi.payment.application.event.TransactionCreateEvent;
import com.sparta.deliveryi.transaction.application.dto.TransactionCreateCommand;
import com.sparta.deliveryi.transaction.application.service.TransactionApplication;
import com.sparta.deliveryi.user.application.service.UserApplication;
import com.sparta.deliveryi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class TransactionEventHandler {

    private final TransactionApplication transactionApplication;
    private final UserApplication userApplication;
    private final ResourceLoader resourceLoader;

    @Async
    @TransactionalEventListener(TransactionCreateEvent.class)
    public void handleTransactionCreateEvent(TransactionCreateEvent event) {
        User user = userApplication.getUserById(event.userId());
        TransactionCreateCommand command = new TransactionCreateCommand(
                event.paymentId(),
                event.amount(),
                event.type(),
                event.status(),
                event.response(),
                user.getUsername()
        );
        transactionApplication.register(command);
    }
}
