package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderCreateRequest;
import jakarta.validation.Valid;

public interface OrderRegister {
    Order register(@Valid OrderCreateRequest createRequest);
}
