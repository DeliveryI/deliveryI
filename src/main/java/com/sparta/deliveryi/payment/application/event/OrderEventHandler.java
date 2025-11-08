package com.sparta.deliveryi.payment.application.event;

import com.sparta.deliveryi.order.event.OrderCreateEvent;
import com.sparta.deliveryi.payment.domain.service.PaymentCreate;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OrderEventHandler {

    private final UserQuery userQuery;
    private final PaymentCreate paymentCreate;

    @Async
    @TransactionalEventListener(OrderCreateEvent.class)
    public void handleOrderCreateEvent(OrderCreateEvent event) {
        User user = userQuery.getUserById(UserId.of(event.requestId()));
        paymentCreate.register(event.orderId().toUuid(), event.amount(), user.getUsername());
    }
}
