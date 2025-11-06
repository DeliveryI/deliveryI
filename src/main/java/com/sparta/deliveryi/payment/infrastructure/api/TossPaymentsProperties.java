package com.sparta.deliveryi.payment.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.payment.application.dto.CancelRequest;
import com.sparta.deliveryi.payment.application.dto.PaymentInfo;
import com.sparta.deliveryi.payment.application.service.TossManageService;
import com.sparta.deliveryi.payment.infrastructure.TossException;
import com.sparta.deliveryi.payment.infrastructure.TossProperties;
import com.sparta.deliveryi.payment.infrastructure.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TossProperties.class)
public class TossPaymentsProperties implements TossManageService {

    private final TossProperties properties;
    private final ObjectMapper objectMapper;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        byte[] encodedBytes = Base64.getEncoder().encode((properties.getEncodedSecretKey() + ":").getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + new String(encodedBytes, StandardCharsets.UTF_8));
        // headers.set("Idempotency-Key", properties.getIdempotencyKey());

        return headers;
    }

    private PaymentInfo handleResponse(ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.convertValue(response.getBody(), PaymentInfo.class);
        }

        HttpStatus status = HttpStatus.resolve(response.getStatusCode().value());
        ErrorResponse error = objectMapper.convertValue(response.getBody(), ErrorResponse.class);

        throw new TossException(error.code(), error.message(), status);
    }

    @Override
    public PaymentInfo getPaymentByOrderId(String orderId) {
        HttpHeaders headers = createHeaders();

        RestClient client = RestClient.create();

        ResponseEntity<String> response = client.get()
                .uri("https://api.tosspayments.com/v1/payments/orders/" + orderId)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(String.class);

        return handleResponse(response);
    }

    @Override
    public PaymentInfo getPaymentByPaymentKey(String paymentKey) {
        HttpHeaders headers = createHeaders();

        RestClient client = RestClient.create();

        ResponseEntity<String> response = client.get()
                .uri("https://api.tosspayments.com/v1/payments/" + paymentKey)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(String.class);

        return handleResponse(response);
    }

    public PaymentInfo confirm(String paymentKey, String orderId, int amount) {
        HttpHeaders headers = createHeaders();

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

        return handleResponse(response);
    }

    @Override
    public PaymentInfo cancel(String paymentKey, CancelRequest request) {
        HttpHeaders headers = createHeaders();

        RestClient client = RestClient.create();

        ResponseEntity<String> response = client.post()
                .uri(String.format("https://api.tosspayments.com/v1/payments/%s/cancel", paymentKey))
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(String.class);

        return handleResponse(response);
    }
}
