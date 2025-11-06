package com.sparta.deliveryi.order;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderCreateRequest;
import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.OrderItem;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderFixture {

    public static Order createOrder(OrderCreateRequest request) {
        Order order = Order.create(request);
        ReflectionTestUtils.setField(order, "createdAt", LocalDateTime.now());

        return order;
    }

    public static OrderCreateRequest createOrderCreateRequest() {
        OrderItem orderItem1 = OrderItem.of(1L, 5000, 2);
        OrderItem orderItem2 = OrderItem.of(2L, 10000, 1);

        return new OrderCreateRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(orderItem1, orderItem2),
                "서울시 강남구 테스트로 13"
        );
    }
}
