package com.sparta.deliveryi.payment.presentation;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.payment.application.dto.PaymentConfirmCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentFailCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;
import com.sparta.deliveryi.payment.application.dto.PaymentSearchRequest;
import com.sparta.deliveryi.payment.application.service.PaymentApplication;
import com.sparta.deliveryi.payment.domain.Payment;
import com.sparta.deliveryi.payment.presentation.dto.PaymentFailRequest;
import com.sparta.deliveryi.payment.presentation.dto.PaymentInfoResponse;
import com.sparta.deliveryi.payment.presentation.dto.PaymentSuccessRequest;
import com.sparta.deliveryi.payment.presentation.dto.PaymentSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.success;
import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
@Tag(name = "결제 API", description = "")
public class PaymentApi {

    private final PaymentApplication paymentApplication;

    @Operation(summary = "결제 승인 요청",
            description = "결제 요청이 성공하여, 결제 승인을 진행합니다.")
    @PostMapping("/success")
    public ResponseEntity<ApiResponse<PaymentSuccessResponse>> requestSuccess(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PaymentSuccessRequest request
    ) {
        PaymentConfirmCommand command = new PaymentConfirmCommand(request.paymentKey(), request.orderId(), request.amount());
        PaymentResponse response = paymentApplication.confirm(UUID.fromString(jwt.getSubject()), command);
        return ok(successWithDataOnly(PaymentSuccessResponse.from(response)));
    }

    @Operation(summary = "결제 요청 실패", description = "결제 요청에 실패하였습니다.")
    @PostMapping("/fail")
    public ResponseEntity<ApiResponse<Void>> requestFail(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PaymentFailRequest request
    ) {
        PaymentFailCommand command = new PaymentFailCommand(request.orderId(), request.code(), request.message());
        paymentApplication.fail(UUID.fromString(jwt.getSubject()), command);
        log.info("결제 요청에 실패했습니다. {code={}, message={}}", request.code(), request.message());
        return ok(success());
    }

    @Operation(summary = "결제 조회", description = "주문ID에 대한 결제 내역을 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<PaymentInfoResponse>> getPaymentByOrderId(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId
    ) {
        Payment payment = paymentApplication.getPaymentByOrderId(UUID.fromString(jwt.getSubject()), orderId);
        return ok(successWithDataOnly(PaymentInfoResponse.from(payment)));
    }

    @Operation(summary = "결제 목록 조회(관리자용)", description = "모든 결제 내역을 조회합니다.")
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<PaymentInfoResponse>>> getPaymentByOrderId(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam PaymentSearchRequest search,
            @RequestParam Pageable pageable
    ) {
        Page<Payment> payments = paymentApplication.searchPayments(UUID.fromString(jwt.getSubject()), search, pageable);
        Page<PaymentInfoResponse> response = payments.map(PaymentInfoResponse::from);
        return ok(successWithDataOnly(response));
    }
}
