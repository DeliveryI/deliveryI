package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.order.OrderFixture;
import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderException;
import com.sparta.deliveryi.order.domain.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
record OrderManagerTest(
        OrderCreator orderCreator,
        OrderManager orderManager,
        OrderFinder orderFinder,
        EntityManager entityManager
) {

    @Test
    void changeDeliveryAddress() {
        Order order = registerOrder();

        String address = "서울시 강남구 테스트로 12";
        orderManager.changeDeliveryAddress(order.getId(), address, order.getOrderer().getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getDeliveryAddress()).isEqualTo(address);
    }

    @Test
    void changeDeliveryAddressIfNotOrderer() {
        Order order = registerOrder();

        assertThatThrownBy(() -> orderManager.changeDeliveryAddress(order.getId(), "서울시 강남구 테스트로 12", UUID.randomUUID()))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void failPayment() {
        Order order = registerOrder();

        orderManager.failPayment(order.getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.PAYMENT_FAILED);
    }

    @Test
    void successPayment() {
        Order order = registerOrder();

        orderManager.successPayment(order.getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
    }

    @Test
    void accept() {
        Order order = registerOrder();

        orderManager.accept(order.getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_ACCEPTED);
    }

    @Test
    void reject() {
        Order order = registerOrder();

        orderManager.reject(order.getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_REJECTED);
    }

    @Test
    void cancel() {
        Order order = registerOrder();

        orderManager.cancel(order.getId(), order.getOrderer().getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_CANCELED);
    }

    @Test
    void cancelIfNotOrderer() {
        Order order = registerOrder();

        assertThatThrownBy(() -> orderManager.cancel(order.getId(), UUID.randomUUID()))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void completeCooking() {
        Order order = registerOrder();

        orderManager.completeCooking(order.getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.READY_TO_SERVED);
    }

    @Test
    void delivery() {
        Order order = registerOrder();

        orderManager.delivery(order.getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.DELIVERING);
    }

    @Test
    void complete() {
        Order order = registerOrder();

        orderManager.complete(order.getId());
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_COMPLETED);
    }

    private Order registerOrder() {
        Order order = orderCreator.create(OrderFixture.createOrderCreateRequest());
        entityManager.flush();
        entityManager.clear();

        return order;
    }
}