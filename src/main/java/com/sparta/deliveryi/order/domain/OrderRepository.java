package com.sparta.deliveryi.order.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Order, OrderId> {
    Order save(Order order);

    Optional<Order> findById(OrderId orderId);
}
