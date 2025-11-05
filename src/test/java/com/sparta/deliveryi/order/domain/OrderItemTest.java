package com.sparta.deliveryi.order.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    void totalPrice() {
        OrderItem item = OrderItem.of(1L, 2000, 4);

        assertThat(item.totalPrice()).isEqualTo(8000);
    }

}