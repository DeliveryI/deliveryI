package com.sparta.deliveryi.user.infrastructure.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "keyclock")
public class KeyclakProperties {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String adminUsername;
    private String adminPassword;
}
