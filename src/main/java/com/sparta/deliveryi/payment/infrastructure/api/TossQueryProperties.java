package com.sparta.deliveryi.payment.infrastructure.api;

import com.sparta.deliveryi.payment.infrastructure.dto.PaymentResponse;
import com.sparta.deliveryi.payment.application.service.TossQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class TossQueryProperties implements TossQueryService {

    private final TossProvider provider;

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        HttpHeaders headers = provider.createHeaders();

        RestClient client = RestClient.create();

        ResponseEntity<String> response = client.get()
                .uri("https://api.tosspayments.com/v1/payments/orders/" + orderId)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(String.class);

        return provider.handleResponse(response);
    }

    @Override
    public PaymentResponse getPaymentByPaymentKey(String paymentKey) {
        HttpHeaders headers = provider.createHeaders();

        RestClient client = RestClient.create();

        ResponseEntity<String> response = client.get()
                .uri("https://api.tosspayments.com/v1/payments/" + paymentKey)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(String.class);

        return provider.handleResponse(response);
    }
}
