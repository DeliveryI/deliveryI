package com.sparta.deliveryi.payment.infrastructure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "toss")
public class TossProperties {
    String encodedSecretKey;
    String IdempotencyKey;
}
