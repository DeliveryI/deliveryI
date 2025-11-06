package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService implements OrderFinder {

    private final OrderRepository orderRepository;

    @Override
    public Order find(OrderId orderId) {
        return orderRepository.findById(orderId.value())
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다. id: " + orderId));
    }

}
