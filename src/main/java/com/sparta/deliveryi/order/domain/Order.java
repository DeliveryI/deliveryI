package com.sparta.deliveryi.order.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.global.infrastructure.event.Events;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
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

    public void changeDeliveryAddress(String address) {
        validateChangeable(address);

        this.deliveryAddress = address;
    }

    public void failPayment() {
        validatePaymentAvailable();

        this.status = OrderStatus.PAYMENT_FAILED;
    }

    public void successPayment() {
        validatePaymentAvailable();

        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    public void accept() {
        validateProcessable();

        this.status = OrderStatus.ORDER_ACCEPTED;
    }

    public void reject() {
        validateProcessable();

        this.status = OrderStatus.ORDER_REJECTED;

        Events.trigger(new OrderRejectEvent(id, totalPrice));
    }

    public void cancel() {
        validateCancelable();

        this.status = OrderStatus.ORDER_CANCELED;

        Events.trigger(new OrderCancelEvent(getId(), totalPrice));
    }

    public void completeCooking() {
        validateAccepted();

        this.status = OrderStatus.READY_TO_SERVED;
    }

    public void delivery() {
        validateReadyToServed();

        this.status = OrderStatus.DELIVERING;
    }

    public void complete() {
        validateDelivering();

        this.status = OrderStatus.ORDER_COMPLETED;
    }

    public Integer calculateTotalPrice() {
        return this.orderDetails.stream()
                .map(OrderDetail::getOrderItem)
                .mapToInt(OrderItem::totalPrice)
                .sum();
    }

    public OrderId getId() {
        return OrderId.of(id);
    }

    private void validateDelivering() {
        if (status != OrderStatus.DELIVERING) {
            throw new OrderException(OrderMessageCode.NOT_READY_TO_SERVED_STATUS);
        }
    }

    private void validateReadyToServed() {
        if (status != OrderStatus.READY_TO_SERVED) {
            throw new OrderException(OrderMessageCode.NOT_READY_TO_SERVED_STATUS);
        }
    }

    private void validateAccepted() {
        if (this.status != OrderStatus.ORDER_ACCEPTED) {
            throw new OrderException(OrderMessageCode.NOT_ACCEPTED_STATUS);
        }
    }

    private void validateNotCanceled() {
        if (status == OrderStatus.ORDER_CANCELED) {
            throw new OrderException(OrderMessageCode.ORDER_ALREADY_CANCELED);
        }
    }

    private void validatePaymentAvailable() {
        validateNotCanceled();

        if (isPaymentComplete()) {
            throw new OrderException(OrderMessageCode.PAYMENT_ALREADY_COMPLETED);
        }
    }

    private void validateProcessable() {
        validateNotCanceled();

        if (status != OrderStatus.PAYMENT_COMPLETED) {
            throw new OrderException(OrderMessageCode.BEFORE_PAYMENT);
        }
    }

    private void validateCancelable() {
        validateNotCanceled();

        if (isCancelTimeout()) {
            throw new OrderException(OrderMessageCode.CANCEL_TIMEOUT);
        }

        if (isAcceptedOrLater()) {
            throw new OrderException(OrderMessageCode.CANNOT_CANCEL_STATUS);
        }
    }

    private void validateChangeable(String address) {
        validateValidAddress(address);
        validateBeforeDelivering();
    }

    private void validateValidAddress(String address) {
        if (StringUtils.isBlank(address)) {
            throw new OrderException(OrderMessageCode.INVALID_ADDRESS);
        }
    }

    private void validateBeforeDelivering() {
        if (isDeliveringOrLater()) {
            throw new OrderException(OrderMessageCode.DELIVERING_OR_COMPLETED_STATUS);
        }
    }

    private boolean isDeliveringOrLater() {
        return this.status == OrderStatus.DELIVERING
                || this.status == OrderStatus.ORDER_COMPLETED;
    }

    private boolean isPaymentComplete() {
        return this.status == OrderStatus.PAYMENT_COMPLETED
                || this.status == OrderStatus.ORDER_ACCEPTED
                || this.status == OrderStatus.READY_TO_SERVED
                || this.status == OrderStatus.DELIVERING
                || this.status == OrderStatus.ORDER_COMPLETED;
    }

    private boolean isCancelTimeout() {
        return LocalDateTime.now().isAfter(getCreatedAt().plusMinutes(5L));
    }

    private boolean isAcceptedOrLater() {
        return this.status == OrderStatus.ORDER_ACCEPTED
                || this.status == OrderStatus.READY_TO_SERVED
                || this.status == OrderStatus.DELIVERING
                || this.status == OrderStatus.ORDER_COMPLETED;
    }
}
