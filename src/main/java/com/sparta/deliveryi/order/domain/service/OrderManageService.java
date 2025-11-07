package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.OrderRepository;
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
    }

    @Override
    public void successPayment(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.successPayment();
    }

    @Override
    public void accept(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.accept();
    }

    @Override
    public void reject(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.reject();
    }

    @Override
    public void cancel(OrderId orderId, UUID requestId) {
        Order order = orderFinder.find(orderId);

        checkOrderer(requestId, order);

        order.cancel();
    }

    @Override
    public void completeCooking(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.completeCooking();
    }

    @Override
    public void delivery(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.delivery();
    }

    @Override
    public void complete(OrderId orderId) {
        Order order = orderFinder.find(orderId);

        order.complete();
    }
}
