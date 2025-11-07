package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.OrderRepository;
import com.sparta.deliveryi.order.event.OrderCancelEvent;
import com.sparta.deliveryi.order.event.OrderRejectEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class OrderManageService implements OrderManager {

    private final OrderRepository orderRepository;

    private final OrderFinder orderFinder;

    private static void checkOrderer(UUID requestId, Order order) {
        if (!order.getOrderer().getId().equals(requestId)) {
            throw new IllegalArgumentException("주문자 정보가 일치하지 않습니다.");
        }
    }

    @Override
    public Order changeDeliveryAddress(OrderId orderId, String deliveryAddress, UUID requestId) {
        Order order = orderFinder.find(orderId);

        checkOrderer(requestId, order);

        order.changeDeliveryAddress(deliveryAddress);

        return orderRepository.save(order);
    }

    @Override
    public void failPayment(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.failPayment();

        orderRepository.save(order);
    }

    @Override
    public void successPayment(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.successPayment();

        orderRepository.save(order);
    }

    @Override
    public void accept(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.accept();

        orderRepository.save(order);
    }

    @Override
    public void reject(OrderId orderId, UUID requestId) {
        Order order = orderFinder.find(orderId);

        order.reject();

        order = orderRepository.save(order);

        Events.trigger(new OrderRejectEvent(orderId.toUuid(), "가게 주인이 주문 거절하였습니다.", order.getTotalPrice(), requestId));
    }

    @Override
    public void cancel(OrderId orderId, UUID requestId) {
        Order order = orderFinder.find(orderId);

        checkOrderer(requestId, order);

        order.cancel();

        order = orderRepository.save(order);

        Events.trigger(new OrderCancelEvent(order.getId(), order.getTotalPrice(), requestId));
    }

    @Override
    public void completeCooking(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.completeCooking();

        orderRepository.save(order);
    }

    @Override
    public void delivery(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.delivery();

        orderRepository.save(order);
    }

    @Override
    public void complete(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.complete();

        orderRepository.save(order);
    }
}
