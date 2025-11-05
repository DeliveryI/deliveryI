package com.sparta.deliveryi.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Column(name = "menu_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    private OrderItem(Long menuId, Integer price, Integer quantity) {
        this.id = menuId;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem of(Long menuId, Integer price, Integer quantity) {
        return new OrderItem(menuId, price, quantity);
    }

    public Integer totalPrice() {
        return quantity * price;
    }
}
