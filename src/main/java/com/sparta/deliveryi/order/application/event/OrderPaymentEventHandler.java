package com.sparta.deliveryi.order.application.event;

import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.service.OrderManager;
import com.sparta.deliveryi.payment.application.event.PaymentFailEvent;
import com.sparta.deliveryi.payment.application.event.PaymentSuccessEvent;
import com.sparta.deliveryi.user.application.service.UserApplication;
import com.sparta.deliveryi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OrderPaymentEventHandler {

    private final OrderManager orderManager;

    private final UserApplication userApplication;

    @Async
    @TransactionalEventListener(PaymentSuccessEvent.class)
    public void handle(PaymentSuccessEvent event) {
        User user = userApplication.getUserById(event.requestId());
        OrderId orderId = OrderId.of(event.orderId());

        orderManager.successPayment(orderId, user.getUsername());
    }

    @Async
    @TransactionalEventListener(PaymentFailEvent.class)
    public void handle(PaymentFailEvent event) {
        User user = userApplication.getUserById(event.requestId());
        OrderId orderId = OrderId.of(event.orderId());

        orderManager.failPayment(orderId, user.getUsername());
    }
}
