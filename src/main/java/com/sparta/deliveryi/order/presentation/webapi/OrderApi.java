package com.sparta.deliveryi.order.presentation.webapi;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.order.application.service.OrderApplication;
import com.sparta.deliveryi.order.domain.*;
import com.sparta.deliveryi.order.domain.service.OrderCreator;
import com.sparta.deliveryi.order.domain.service.OrderFinder;
import com.sparta.deliveryi.order.domain.service.OrderManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "주문 API", description = """
주문 요청(Order Requested)부터 주문 완료(Order Completed)까지의 주문 처리 단계 관련 기능 제공.
- 주문 생성 / 조회 / 취소
- 주문 수락 / 거절 / 조리 완료 / 배달 시작 / 주문 완료
""")
@RestController
@RequiredArgsConstructor
public class OrderApi {

    private final OrderFinder orderFinder;

    private final OrderCreator orderCreator;

    private final OrderManager orderManager;

    private final OrderApplication orderApplication;

    @Operation(
            summary = "주문 요청 (Order Requested)",
            description = """
            고객이 신규 주문을 생성합니다.
            주문 생성 시 상태는 기본적으로 '주문 요청(Order Requested)'으로 설정됩니다.
            """
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders")
    public ResponseEntity<ApiResponse<OrderCreateResponse>> create(@RequestBody @Valid OrderCreateRequest createRequest) {
        Order order = orderCreator.create(createRequest);

        return ok(ApiResponse.successWithDataOnly(OrderCreateResponse.from(order)));
    }

    @Operation(
            summary = "주문 검색",
            description = "상점 및 주문자 기준으로 주문 목록을 조회합니다."
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @GetMapping("/v1/orders")
    public ResponseEntity<ApiResponse<Page<OrderSearchResponse>>> search(
            @Parameter(name = "storeId", description = "가게 ID (UUID)", in = ParameterIn.QUERY, required = true)
            @RequestParam UUID storeId,
            @Parameter(name = "ordererId", description = "주문자 ID (UUID)", in = ParameterIn.QUERY, required = true)
            @RequestParam UUID ordererId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        OrderSearchCondition condition = OrderSearchCondition.of(storeId, Orderer.of(ordererId), pageable);
        Page<Order> orders = orderFinder.search(condition);

        Page<OrderSearchResponse> responses = orders.map(OrderSearchResponse::from);

        return ok(successWithDataOnly(responses));
    }

    @Operation(
            summary = "배달 주소 변경",
            description = """
            주문 수락(Order Accepted) 상태 이전의 주문에 대해 배달 주소를 수정합니다.
            """
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PutMapping("/v1/orders/{orderId}/delivery-address")
    public ResponseEntity<ApiResponse<ChangeDeliveryAddressResponse>> changeDeliveryAddress(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "orderId", description = "주문 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID orderId,
            @RequestBody @Valid ChangeDeliveryAddressRequest changeRequest
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());
        Order order = orderApplication.changeDeliveryAddress(orderId, changeRequest.deliveryAddress(), requestId);

        return ok(ApiResponse.successWithDataOnly(ChangeDeliveryAddressResponse.from(order)));
    }

    @Operation(
            summary = "주문 수락 (Order Accepted)",
            description = """
            주문을 수락합니다.
            주문 상태가 '주문 수락(Order Accepted)'으로 변경됩니다.
            """
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/accept")
    public ResponseEntity<ApiResponse<Void>> accept(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "orderId", description = "주문 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID orderId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.accept(orderId, requestId);

        return ok(success());
    }

    @Operation(
            summary = "주문 거절 (Order Rejected)",
            description = """
            주문을 거절합니다.
            주문 상태가 '주문 거절(Order Rejected)'로 변경됩니다.
            """
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "orderId", description = "주문 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID orderId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.reject(orderId, requestId);

        return ok(success());
    }

    @Operation(
            summary = "주문 취소 (Order Canceled)",
            description = """
            주문을 취소합니다.
            주문 상태가 '주문 취소(Order Canceled)'로 변경됩니다.
            """
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "orderId", description = "주문 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID orderId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderManager.cancel(OrderId.of(orderId), requestId);

        return ok(success());
    }

    @Operation(
            summary = "조리 완료 (Ready To Served)",
            description = """
            점주(OWNER) 또는 관리자(MANAGER/MASTER)가 조리가 완료되었음을 알립니다.
            주문 상태가 '주문 수락(Order Accepted)' → '조리 완료(Ready To Served)'로 변경됩니다.
            """
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/complete-cooking")
    public ResponseEntity<ApiResponse<Void>> completeCooking(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "orderId", description = "주문 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID orderId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.completeCooking(orderId, requestId);

        return ok(success());
    }

    @Operation(
            summary = "배달 중 (Delivering)",
            description = """
            점주(OWNER) 또는 관리자(MANAGER/MASTER)가 배달을 시작합니다.
            주문 상태가 '조리 완료(Ready To Served)' → '배달 중(Delivering)'으로 변경됩니다.
            """
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/delivery")
    public ResponseEntity<ApiResponse<Void>> delivery(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "orderId", description = "주문 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID orderId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.delivery(orderId, requestId);

        return ok(success());
    }

    @Operation(
            summary = "주문 완료 (Order Completed)",
            description = """
            배달이 완료되어 주문을 마무리합니다.
            주문 상태가 '배달 중(Delivering)' → '주문 완료(Order Completed)'으로 변경됩니다.
            """
    )
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @PostMapping("/v1/orders/{orderId}/complete")
    public ResponseEntity<ApiResponse<Void>> complete(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(name = "orderId", description = "주문 ID (UUID)", in = ParameterIn.PATH, required = true)
            @PathVariable UUID orderId
    ) {
        UUID requestId = UUID.fromString(jwt.getSubject());

        orderApplication.complete(orderId, requestId);

        return ok(success());
    }

}