package com.sparta.deliveryi.order.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.Objects.requireNonNull;

@Table(name = "p_order_detail")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @Embedded
    private OrderItem orderItem;

    public static OrderDetail of(Order order, OrderItem orderItem) {
        OrderDetail orderDetail = new OrderDetail();

        orderDetail.order = requireNonNull(order);
        orderDetail.orderItem = requireNonNull(orderItem);

        return orderDetail;
    }

}
