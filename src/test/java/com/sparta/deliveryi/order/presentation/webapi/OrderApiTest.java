package com.sparta.deliveryi.order.presentation.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.AssertThatUtils;
import com.sparta.deliveryi.order.OrderFixture;
import com.sparta.deliveryi.order.application.service.OrderApplication;
import com.sparta.deliveryi.order.domain.Order;
import com.sparta.deliveryi.order.domain.OrderCreateRequest;
import com.sparta.deliveryi.order.domain.service.OrderCreator;
import com.sparta.deliveryi.order.domain.service.OrderManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.UUID;

import static com.sparta.deliveryi.AssertThatUtils.isFalse;
import static com.sparta.deliveryi.AssertThatUtils.isTrue;
import static com.sparta.deliveryi.MockUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiTest {

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void create() throws JsonProcessingException {
        OrderCreateRequest request = OrderFixture.createOrderCreateRequest();
        String requestJson = objectMapper.writeValueAsString(request);
        Order order = OrderFixture.createOrder();
        given(creator.create(any(OrderCreateRequest.class))).willReturn(order);

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue())
                .hasPathSatisfying("$.data", AssertThatUtils.notNull());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void changeDeliveryAddress() throws JsonProcessingException {
        ChangeDeliveryAddressRequest request = new ChangeDeliveryAddressRequest("서울시 강남구 테스트로 12");
        String requestJson = objectMapper.writeValueAsString(request);
        Order order = OrderFixture.createOrder();
        given(orderApplication.changeDeliveryAddress(eq(order.getId().toUuid()), anyString(), eq(UUID.fromString(customerId())))).willReturn(order);
        mockedCustomerToken();

        MvcTestResult result = mvcTester.put()
                .uri("/v1/orders/{orderId}/delivery-address", order.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue())
                .hasPathSatisfying("$.data", AssertThatUtils.notNull());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void accept() {
        Order order = OrderFixture.createOrder();
        doNothing().when(orderApplication).accept(eq(order.getId().toUuid()), eq(UUID.fromString(customerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/accept", order.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void acceptIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/accept", UUID.randomUUID().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void reject() {
        Order order = OrderFixture.createOrder();
        doNothing().when(orderApplication).reject(eq(order.getId().toUuid()), eq(UUID.fromString(customerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/reject", order.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void rejectIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/reject", UUID.randomUUID().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void cancel() {
        Order order = OrderFixture.createOrder();
        doNothing().when(orderManager).cancel(eq(order.getId()), eq(UUID.fromString(customerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/cancel", order.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void completeCooking() {
        Order order = OrderFixture.createOrder();
        doNothing().when(orderApplication).completeCooking(eq(order.getId().toUuid()), eq(UUID.fromString(customerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/complete-cooking", order.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void completeCookingIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/complete-cooking", UUID.randomUUID().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void delivery() {
        Order order = OrderFixture.createOrder();
        doNothing().when(orderApplication).delivery(eq(order.getId().toUuid()), eq(UUID.fromString(customerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/delivery", order.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void deliveryIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/delivery", UUID.randomUUID().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void complete() {
        Order order = OrderFixture.createOrder();
        doNothing().when(orderApplication).complete(eq(order.getId().toUuid()), eq(UUID.fromString(customerId())));
        mockedOwnerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/complete", order.getId().toString())
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.success", isTrue());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void completeIfUnauthorized() {
        mockedCustomerToken();

        MvcTestResult result = mvcTester.post()
                .uri("/v1/orders/{orderId}/complete", UUID.randomUUID().toString())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FORBIDDEN)
                .bodyJson()
                .hasPathSatisfying("$.success", isFalse());
    }
    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OrderCreator creator;

    @MockitoBean
    OrderManager manager;

    @MockitoBean
    OrderApplication application;

    @Autowired
    private OrderApplication orderApplication;

    @Autowired
    private OrderManager orderManager;
}