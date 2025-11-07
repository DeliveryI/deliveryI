package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface OrderManager {
    Order changeDeliveryAddress(@NotNull OrderId orderId, @Valid String deliveryAddress, @NotNull UUID requestId);

    void failPayment(@NotNull OrderId orderId);

    void successPayment(@NotNull OrderId orderId);

    void accept(@NotNull OrderId orderId);

    void reject(@NotNull OrderId orderId);

    void cancel(@NotNull OrderId orderId, @NotNull UUID requestId);

    void completeCooking(@NotNull OrderId orderId);

    void delivery(@NotNull OrderId orderId);

    void complete(@NotNull OrderId orderId);

}
