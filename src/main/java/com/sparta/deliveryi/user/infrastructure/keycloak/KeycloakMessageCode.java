package com.sparta.deliveryi.user.infrastructure.keycloak;

import com.sparta.deliveryi.global.exception.MessageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KeycloakMessageCode implements MessageCode {
    NOT_FOUND("KEYCLOAK.NOT_FOUND", HttpStatus.NOT_FOUND);

    private final String code;
    private final HttpStatus status;
}
