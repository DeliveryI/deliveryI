package com.sparta.deliveryi.order.application.service;

import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderId;
import com.sparta.deliveryi.order.domain.service.OrderFinder;
import com.sparta.deliveryi.order.domain.service.OrderManager;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderApplicationService implements OrderApplication {

    private final OrderManager orderManager;

    private final OrderFinder orderFinder;

    private final StoreFinder storeFinder;

    private final UserRolePolicy userRolePolicy;

    private static void validateCanChange(Order order, UUID resolveRequestId) {
        if (!order.getOrderer().getId().equals(resolveRequestId)) {
            throw new IllegalArgumentException("변경 권한이 없습니다.");
        }
    }

    @Override
    public Order changeDeliveryAddress(UUID orderId, String deliveryAddress, UUID requestId) {
        Order order = orderFinder.find(OrderId.of(orderId));

        UUID resolveRequestId = resolveRequestId(requestId, order);

        validateCanChange(order, resolveRequestId);

        return orderManager.changeDeliveryAddress(OrderId.of(orderId), deliveryAddress, resolveRequestId);
    }

    @Override
    public void accept(UUID orderId, UUID requestId) {
        validateCanProcess(orderId, requestId);

        orderManager.accept(OrderId.of(orderId));
    }

    @Override
    public void reject(UUID orderId, UUID requestId) {
        validateCanProcess(orderId, requestId);

        orderManager.reject(OrderId.of(orderId));
    }

    @Override
    public void completeCooking(UUID orderId, UUID requestId) {
        validateCanProcess(orderId, requestId);

        orderManager.completeCooking(OrderId.of(orderId));
    }

    @Override
    public void delivery(UUID orderId, UUID requestId) {
        validateCanProcess(orderId, requestId);

        orderManager.delivery(OrderId.of(orderId));
    }

    @Override
    public void complete(UUID orderId, UUID requestId) {
        validateCanProcess(orderId, requestId);

        orderManager.complete(OrderId.of(orderId));
    }

    private UUID resolveRequestId(UUID requestId, Order order) {
        return isAdmin(requestId) ? order.getOrderer().getId() : requestId;
    }

    private void validateCanProcess(UUID orderId, UUID requestId) {
        if (!isOwnerOrAdmin(orderId, requestId)) {
            throw new IllegalArgumentException("작업 권한이 없습니다.");
        }
    }


    private boolean isOwnerOrAdmin(UUID orderId, UUID requestId) {
        if (isAdmin(requestId)) {
            return true;
        }

        Order order = orderFinder.find(OrderId.of(orderId));
        StoreId storeId = StoreId.of(order.getStoreId());
        Store store = storeFinder.find(storeId);

        return store.getOwner().getId().equals(requestId);
    }

    private boolean isAdmin(UUID requestId) {
        return userRolePolicy.isAdmin(requestId);
    }
}
