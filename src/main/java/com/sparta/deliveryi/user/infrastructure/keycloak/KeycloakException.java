package com.sparta.deliveryi.user.infrastructure.keycloak;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class KeycloakException extends AbstractException {
    public KeycloakException(MessageCode messageCode) {
        super(messageCode);
    }

    public KeycloakException(MessageCode messageCode, String message) {
        super(messageCode, message);
    }

    public KeycloakException(MessageCode messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    public KeycloakException(MessageCode messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
    }
}
