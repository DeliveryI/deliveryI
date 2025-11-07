package com.sparta.deliveryi.payment.presentation;

import com.sparta.deliveryi.global.presentation.dto.ApiResponse;
import com.sparta.deliveryi.payment.application.dto.PaymentConfirmCommand;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;
import com.sparta.deliveryi.payment.application.service.PaymentApplication;
import com.sparta.deliveryi.payment.presentation.dto.PaymentConfirmRequest;
import com.sparta.deliveryi.payment.presentation.dto.PaymentConfirmResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.sparta.deliveryi.global.presentation.dto.ApiResponse.successWithDataOnly;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
@Tag(name = "결제 API", description = "")
public class PaymentApi {

    private final PaymentApplication paymentApplication;

    @Operation(summary = "결제 승인 요청",
            description = "결제 요청이 성공하여, 결제 승인을 진행합니다.")
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmResponse>> requestSuccess(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PaymentConfirmRequest request
    ) {
        PaymentConfirmCommand command = new PaymentConfirmCommand(request.paymentKey(), request.orderId(), request.amount());
        PaymentResponse response = paymentApplication.confirm(UUID.fromString(jwt.getSubject()), command);
        return ok(successWithDataOnly(PaymentConfirmResponse.from(response)));
    }

}
