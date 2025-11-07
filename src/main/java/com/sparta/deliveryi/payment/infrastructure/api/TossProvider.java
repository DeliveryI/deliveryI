package com.sparta.deliveryi.payment.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.deliveryi.payment.application.dto.PaymentResponse;
import com.sparta.deliveryi.payment.infrastructure.TossProperties;
import com.sparta.deliveryi.payment.infrastructure.dto.PaymentInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TossProperties.class)
class TossProvider {

    private final TossProperties properties;
    private final ObjectMapper objectMapper;

    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        byte[] encodedBytes = Base64.getEncoder().encode((properties.getEncodedSecretKey() + ":").getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + new String(encodedBytes, StandardCharsets.UTF_8));
        // headers.set("Idempotency-Key", properties.getIdempotencyKey());

        return headers;
    }

    public PaymentResponse handleResponse(ResponseEntity<String> response) {
        try {
            PaymentInfo paymentInfo = objectMapper.readValue(response.getBody(), PaymentInfo.class);

            int httpStatus = response.getStatusCode().value();

            Map<String, Object> result = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            Map<String, String> failure = (Map<String, String>) result.get("failure");
            String code = failure.get("code");
            String message = failure.get("message");

            log.info("Toss API를 호출합니다. {code={}, message={}}", code, message);

            return new PaymentResponse(httpStatus, code, message, paymentInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
