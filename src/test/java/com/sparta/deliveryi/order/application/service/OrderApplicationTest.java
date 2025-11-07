package com.sparta.deliveryi.order.application.service;

import com.sparta.deliveryi.order.OrderFixture;
import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderStatus;
import com.sparta.deliveryi.order.domain.service.OrderCreator;
import com.sparta.deliveryi.order.domain.service.OrderFinder;
import com.sparta.deliveryi.order.domain.service.OrderManager;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Owner;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class OrderApplicationTest {

    UUID requestId = UUID.randomUUID();

    Order order;

    Store store;

    @BeforeEach
    void setUp() {
        order = createOrder();
        store = StoreFixture.createStore();
    }

    @Test
    void changeDeliveryAddress() {
        UUID ordererId = order.getOrderer().getId();
        when(userRolePolicy.isAdmin(eq(ordererId))).thenReturn(false);
        String deliveryAddress = "서울시 강남구 테스트로 12";

        orderApplication.changeDeliveryAddress(order.getId().toUuid(), deliveryAddress, ordererId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getDeliveryAddress()).isEqualTo(deliveryAddress);
    }

    @Test
    void changeDeliveryAddressIfManager() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);
        String deliveryAddress = "서울시 강남구 테스트로 12";

        orderApplication.changeDeliveryAddress(order.getId().toUuid(), deliveryAddress, requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getDeliveryAddress()).isEqualTo(deliveryAddress);
    }

    @Test
    void changeDeliveryAddressIfNotOrderer() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);

        assertThatThrownBy(() -> orderApplication.changeDeliveryAddress(order.getId().toUuid(), "서울시 강남구 테스트로 12", requestId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void accept() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);
        ReflectionTestUtils.setField(store, "owner", Owner.of(requestId));

        orderApplication.accept(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_ACCEPTED);
    }

    @Test
    void acceptIfManager() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);

        orderApplication.accept(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_ACCEPTED);
    }

    @Test
    void acceptIfNotOwner() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);

        assertThatThrownBy(() -> orderApplication.accept(order.getId().toUuid(), requestId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void reject() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);
        ReflectionTestUtils.setField(store, "owner", Owner.of(requestId));

        orderApplication.reject(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_REJECTED);
    }

    @Test
    void rejectIfManager() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);

        orderApplication.reject(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_REJECTED);
    }

    @Test
    void rejectIfNotOwner() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);

        assertThatThrownBy(() -> orderApplication.reject(order.getId().toUuid(), requestId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void completeCooking() {
        orderManager.accept(order.getId());
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);
        ReflectionTestUtils.setField(store, "owner", Owner.of(requestId));

        orderApplication.completeCooking(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.READY_TO_SERVED);
    }

    @Test
    void completeCookingIfManager() {
        orderManager.accept(order.getId());
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);

        orderApplication.completeCooking(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.READY_TO_SERVED);
    }

    @Test
    void completeCookingIfNotOwner() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);

        assertThatThrownBy(() -> orderApplication.completeCooking(order.getId().toUuid(), requestId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delivery() {
        orderManager.accept(order.getId());
        orderManager.completeCooking(order.getId());
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);
        ReflectionTestUtils.setField(store, "owner", Owner.of(requestId));

        orderApplication.delivery(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.DELIVERING);
    }

    @Test
    void deliveryIfManager() {
        orderManager.accept(order.getId());
        orderManager.completeCooking(order.getId());
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);

        orderApplication.delivery(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.DELIVERING);
    }

    @Test
    void deliveryIfNotOwner() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);

        assertThatThrownBy(() -> orderApplication.delivery(order.getId().toUuid(), requestId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void complete() {
        orderManager.accept(order.getId());
        orderManager.completeCooking(order.getId());
        orderManager.delivery(order.getId());
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);
        ReflectionTestUtils.setField(store, "owner", Owner.of(requestId));

        orderApplication.complete(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_COMPLETED);
    }

    @Test
    void completeIfManager() {
        orderManager.accept(order.getId());
        orderManager.completeCooking(order.getId());
        orderManager.delivery(order.getId());
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);

        orderApplication.complete(order.getId().toUuid(), requestId);
        entityManager.flush();
        entityManager.clear();

        Order result = orderFinder.find(order.getId());

        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDER_COMPLETED);
    }

    @Test
    void completeIfNotOwner() {
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        when(storeFinder.find(any(StoreId.class))).thenReturn(store);

        assertThatThrownBy(() -> orderApplication.delivery(order.getId().toUuid(), requestId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order createOrder() {
        Order order = orderCreator.create(OrderFixture.createOrderCreateRequest());
        entityManager.flush();
        entityManager.clear();

        orderManager.successPayment(order.getId());

        return order;
    }

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private OrderFinder orderFinder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderApplication orderApplication;

    @Autowired
    private OrderCreator orderCreator;

    @MockitoBean
    private StoreFinder storeFinder;

    @MockitoBean
    private UserRolePolicy userRolePolicy;
}