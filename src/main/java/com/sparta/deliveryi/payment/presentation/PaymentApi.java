package com.sparta.deliveryi.payment.presentation;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.payment.application.dto.PaymentConfirmCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentFailCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;
import com.sparta.deliveryi.payment.application.service.PaymentApplication;
import com.sparta.deliveryi.payment.presentation.dto.PaymentFailRequest;
import com.sparta.deliveryi.payment.presentation.dto.PaymentSuccessRequest;
import com.sparta.deliveryi.payment.presentation.dto.PaymentSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
