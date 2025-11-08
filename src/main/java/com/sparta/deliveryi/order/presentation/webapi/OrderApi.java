package com.sparta.deliveryi.order.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.order.application.service.OrderApplication;
import com.sparta.deliveryi.order.domain.*;
import com.sparta.deliveryi.order.domain.service.OrderCreator;
import com.sparta.deliveryi.order.domain.service.OrderFinder;
import com.sparta.deliveryi.order.domain.service.OrderManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.success;
import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class OrderApi {

    private final OrderFinder orderFinder;

    private final OrderCreator orderCreator;

    private final OrderManager orderManager;

    private final OrderApplication orderApplication;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders")
    public ResponseEntity<ApiResponse<OrderCreateResponse>> create(@RequestBody @Valid OrderCreateRequest createRequest) {
        Order order = orderCreator.create(createRequest);

        return ok(ApiResponse.successWithDataOnly(OrderCreateResponse.from(order)));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @GetMapping("/v1/orders")
    public ResponseEntity<ApiResponse<Page<OrderSearchResponse>>> search(
            @RequestParam UUID storeId,
            @RequestParam UUID ordererId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        OrderSearchCondition condition = OrderSearchCondition.of(storeId, Orderer.of(ordererId), pageable);
        Page<Order> orders = orderFinder.search(condition);

        Page<OrderSearchResponse> responses = orders.map(OrderSearchResponse::from);

        return ok(successWithDataOnly(responses));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PutMapping("/v1/orders/{orderId}/delivery-address")
    public ResponseEntity<ApiResponse<ChangeDeliveryAddressResponse>> changeDeliveryAddress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId,
            @RequestBody @Valid ChangeDeliveryAddressRequest changeRequest
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());
        Order order = orderApplication.changeDeliveryAddress(orderId, changeRequest.deliveryAddress(), requestId);

        return ok(ApiResponse.successWithDataOnly(ChangeDeliveryAddressResponse.from(order)));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/accept")
    public ResponseEntity<ApiResponse<Void>> accept(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID orderId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.accept(orderId, requestId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID orderId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.reject(orderId, requestId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID orderId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderManager.cancel(OrderId.of(orderId), requestId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/complete-cooking")
    public ResponseEntity<ApiResponse<Void>> completeCooking(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID orderId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.completeCooking(orderId, requestId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/delivery")
    public ResponseEntity<ApiResponse<Void>> delivery(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID orderId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.delivery(orderId, requestId);

        return ok(success());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/complete")
    public ResponseEntity<ApiResponse<Void>> complete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID orderId) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.complete(orderId, requestId);

        return ok(success());
    }

}