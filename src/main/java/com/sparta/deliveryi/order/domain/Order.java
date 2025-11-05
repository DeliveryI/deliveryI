package com.sparta.deliveryi.order.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Table(name = "p_order")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private UUID storeId;

    @Embedded
    private Orderer orderer;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    public static Order create(OrderCreateRequest createRequest) {
        Order order = new Order();

        order.storeId = requireNonNull(createRequest.storeId());
        order.orderer = Orderer.of(createRequest.ordererId());
        order.deliveryAddress = createRequest.deliveryAddress();

        order.status = OrderStatus.ORDER_REQUESTED;

        order.orderDetails = createRequest.orderItems().stream()
                .map(item -> OrderDetail.of(order, item))
                .toList();

        order.totalPrice = order.calculateTotalPrice();

        return order;
    }

    public Integer calculateTotalPrice() {
        return this.orderDetails.stream()
                .map(OrderDetail::getOrderItem)
                .mapToInt(OrderItem::totalPrice)
                .sum();
    }
}
