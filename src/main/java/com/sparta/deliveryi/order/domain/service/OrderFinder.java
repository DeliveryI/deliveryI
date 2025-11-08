package com.sparta.deliveryi.order.domain.service;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.OrderSearchCondition;
import org.springframework.data.domain.Page;

public interface OrderFinder {
    Order find(OrderId orderId);

    Page<Order> search(OrderSearchCondition searchCondition);
}
