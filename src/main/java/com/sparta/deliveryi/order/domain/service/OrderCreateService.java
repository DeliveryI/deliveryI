package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderCreateRequest;
import com.sparta.deliveryi.order.domain.OrderRepository;
import com.sparta.deliveryi.order.event.OrderCreateEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class OrderCreateService implements OrderCreator {

    private final OrderRepository orderRepository;

    @Override
    public Order create(OrderCreateRequest createRequest) {
        Order order = Order.create(createRequest);

        order = orderRepository.save(order);

        Events.trigger(
                new OrderCreateEvent(
                        order.getId(),
                        order.getTotalPrice(),
                        order.getOrderer().getId()
                )
        );

        return order;
    }
}
