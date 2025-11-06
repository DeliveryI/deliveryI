package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.order.OrderFixture;
import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
record OrderCreatorTest(OrderCreator orderCreator, EntityManager entityManager) {

    @Test
    void create() {
        Order order = orderCreator.create(OrderFixture.createOrderCreateRequest());

        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_REQUESTED);
    }
}