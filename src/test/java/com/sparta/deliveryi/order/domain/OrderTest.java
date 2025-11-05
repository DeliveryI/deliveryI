package com.sparta.deliveryi.order.domain;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.order.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.sparta.deliveryi.order.OrderFixture.createOrderCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    Order order;

    @BeforeEach
    void setUp() {
        order = OrderFixture.createOrder(createOrderCreateRequest());
        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    @Test
    void create() {
        OrderCreateRequest request = createOrderCreateRequest();
        List<OrderItem> orderItems = request.orderItems();

        order = OrderFixture.createOrder(request);

        assertThat(order.getStoreId()).isEqualTo(request.storeId());
        assertThat(order.getOrderer()).isEqualTo(Orderer.of(request.ordererId()));
        assertThat(order.getTotalPrice()).isEqualTo(20000);
        assertThat(order.getDeliveryAddress()).isEqualTo(request.deliveryAddress());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_REQUESTED);
        assertThat(order.getOrderDetails().size()).isEqualTo(orderItems.size());
    }
}