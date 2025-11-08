package com.sparta.deliveryi.payment.application.event;

import com.sparta.deliveryi.order.event.OrderCancelEvent;
import com.sparta.deliveryi.order.event.OrderCreateEvent;
import com.sparta.deliveryi.order.event.OrderRejectEvent;
import com.sparta.deliveryi.payment.application.dto.PaymentRefundCommand;
import com.sparta.deliveryi.payment.application.service.PaymentApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OrderEventHandler {

    private final PaymentApplication paymentApplication;

    @Async
    @TransactionalEventListener(OrderCreateEvent.class)
    public void handleOrderCreateEvent(OrderCreateEvent event) {
        paymentApplication.request(event.requestId(), event.orderId().toUuid(), event.amount());
    }

    @Async
    @TransactionalEventListener(OrderCancelEvent.class)
    public void handleOrderCancelEvent(OrderCancelEvent event) {
        PaymentRefundCommand command = new PaymentRefundCommand("주문취소", event.orderId().toUuid(), event.amount());
        paymentApplication.refund(event.requestId(), command);
    }

    @Async
    @TransactionalEventListener(OrderRejectEvent.class)
    public void handleOrderRejectEvent(OrderRejectEvent event) {
        PaymentRefundCommand command = new PaymentRefundCommand(event.reason(), event.orderId(), event.amount());
        paymentApplication.refund(event.requestId(), command);
    }
}
