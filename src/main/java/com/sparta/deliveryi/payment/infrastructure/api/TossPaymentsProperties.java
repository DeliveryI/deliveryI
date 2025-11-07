package com.sparta.deliveryi.payment.infrastructure.api;

import com.sparta.deliveryi.payment.application.dto.PaymentCancelRequest;
import com.sparta.deliveryi.payment.infrastructure.dto.PaymentResponse;
import com.sparta.deliveryi.payment.application.service.TossPaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TossPaymentsProperties implements TossPaymentsService {

    private final TossProvider provider;

    public PaymentResponse confirm(String paymentKey, String orderId, int amount) {
        HttpHeaders headers = provider.createHeaders();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);

        RestClient client = RestClient.create();

        ResponseEntity<String> response = client.post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(requestBody)
                    .retrieve()
                    .toEntity(String.class);

        return provider.handleResponse(response);
    }

    @Override
    public PaymentResponse cancel(String paymentKey, PaymentCancelRequest request) {
        HttpHeaders headers = provider.createHeaders();

        RestClient client = RestClient.create();

        ResponseEntity<String> response = client.post()
                .uri(String.format("https://api.tosspayments.com/v1/payments/%s/cancel", paymentKey))
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(String.class);

        return provider.handleResponse(response);
    }
}
