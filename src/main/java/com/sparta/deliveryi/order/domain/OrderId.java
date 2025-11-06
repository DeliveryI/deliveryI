package com.sparta.deliveryi.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderId {

    @Column(name = "order_id")
    private UUID id;

    private OrderId(UUID id) {
        this.id = id;
    }

    public static OrderId generateId() {
        return of(UUID.randomUUID());
    }

    public static OrderId of(UUID id) {
        return new OrderId(id);
    }

    public UUID toUuid() {
        return id;
    }

    public String toString() {
        return id.toString();
    }
}
