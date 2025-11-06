package com.sparta.deliveryi.order.domain;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.order.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.deliveryi.order.OrderFixture.createOrderCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    Order order;

    @BeforeEach
    void setUp() {
        order = OrderFixture.createOrder(createOrderCreateRequest());

        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    @Test
    void create() {
        OrderCreateRequest request = createOrderCreateRequest();
        List<OrderItem> orderItems = request.orderItems();

        order = OrderFixture.createOrder(request);

        assertThat(order.getId()).isNotNull();
        assertThat(order.getStoreId()).isEqualTo(request.storeId());
        assertThat(order.getOrderer()).isEqualTo(Orderer.of(request.ordererId()));
        assertThat(order.getTotalPrice()).isEqualTo(20000);
        assertThat(order.getDeliveryAddress()).isEqualTo(request.deliveryAddress());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_REQUESTED);
        assertThat(order.getOrderDetails().size()).isEqualTo(orderItems.size());
    }

    @Test
    void changeDeliveryAddress() {
        String address = "변경된 주소";

        order.changeDeliveryAddress(address);

        assertThat(order.getDeliveryAddress()).isEqualTo(address);
    }

    @Test
    void changeDeliveryAddressIfDelivering() {
        order.successPayment();
        order.accept();
        order.completeCooking();
        order.delivery();

        assertThatThrownBy(() -> order.changeDeliveryAddress("변경된 주소"))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void changeDeliveryAddressInvalidAddress() {
        assertThatThrownBy(() -> order.changeDeliveryAddress("  "))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void failPayment() {
        order.failPayment();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_FAILED);
    }

    @Test
    void failPaymentIfCanceled() {
        order.cancel();

        assertThatThrownBy(() -> order.failPayment())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void failPaymentIfPaymentCompleted() {
        order.successPayment();

        assertThatThrownBy(() -> order.failPayment())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void successPayment() {
        order.successPayment();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
    }

    @Test
    void successPaymentIfCanceled() {
        order.cancel();

        assertThatThrownBy(() -> order.successPayment())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void successPaymentIfPaymentCompleted() {
        order.successPayment();

        assertThatThrownBy(() -> order.successPayment())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void accept() {
        order.successPayment();

        order.accept();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_ACCEPTED);
    }

    @Test
    void acceptIfCanceled() {
        order.cancel();

        assertThatThrownBy(() -> order.accept())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void acceptBeforePayment() {
        assertThatThrownBy(() -> order.accept())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void reject() {
        order.successPayment();

        order.reject();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_REJECTED);
    }

    @Test
    void rejectIfCanceled() {
        order.cancel();

        assertThatThrownBy(() -> order.reject())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void rejectBeforePayment() {
        assertThatThrownBy(() -> order.reject())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void cancel() {
        order.cancel();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_CANCELED);
    }

    @Test
    void cancelIfAfterFiveMinute() {
        ReflectionTestUtils.setField(order, "createdAt", LocalDateTime.now().minusMinutes(6L));

        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void cancelIfCanceled() {
        order.cancel();

        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void cancelIfAccepted() {
        order.successPayment();
        order.accept();

        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void completeCooking() {
        order.successPayment();
        order.accept();

        order.completeCooking();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.READY_TO_SERVED);
    }

    @Test
    void completeCookingIfNotAccepted() {
        assertThatThrownBy(() -> order.completeCooking())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void delivery() {
        order.successPayment();
        order.accept();
        order.completeCooking();

        order.delivery();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERING);
    }

    @Test
    void deliveryIfNotReadyToServed() {
        assertThatThrownBy(() -> order.delivery())
                .isInstanceOf(OrderException.class);
    }

    @Test
    void complete() {
        order.successPayment();
        order.accept();
        order.completeCooking();
        order.delivery();

        order.complete();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_COMPLETED);
    }

    @Test
    void completeIfNotDelivering() {
        assertThatThrownBy(() -> order.complete())
                .isInstanceOf(OrderException.class);
    }
}