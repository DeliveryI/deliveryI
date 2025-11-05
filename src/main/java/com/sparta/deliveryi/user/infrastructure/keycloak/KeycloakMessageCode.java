package com.sparta.deliveryi.user.infrastructure.keycloak;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KeycloakMessageCode implements MessageCode {
    INTERNAL_FAILED("KEYCLOAK.INTERNAL_FAILED", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND("KEYCLOAK.USER_NOT_FOUND", HttpStatus.NOT_FOUND);

    private final String code;
    private final HttpStatus status;
}
