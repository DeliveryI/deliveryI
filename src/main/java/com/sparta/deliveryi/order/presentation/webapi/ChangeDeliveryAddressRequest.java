package com.sparta.deliveryi.order.presentation.webapi;

import jakarta.validation.constraints.NotNull;

public record ChangeDeliveryAddressRequest(
        @NotNull String deliveryAddress
) {
}