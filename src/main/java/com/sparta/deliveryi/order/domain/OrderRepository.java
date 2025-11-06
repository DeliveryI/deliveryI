package com.sparta.deliveryi.order.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order Order);

    Optional<Order> findById(Long OrderId);
}
