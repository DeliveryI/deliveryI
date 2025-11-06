package com.sparta.deliveryi.order.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderId {

    private Long id;

    private OrderId(Long id) {
        this.id = id;
    }

    public static OrderId of(Long id) {
        return new OrderId(id);
    }

    public Long value() {
        return id;
    }
}
