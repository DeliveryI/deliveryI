package com.sparta.deliveryi.user.infrastructure.keycloak;

import com.sparta.deliveryi.global.exception.AbstractException;
import com.sparta.deliveryi.global.exception.MessageCode;

public class KeycloakException extends AbstractException {
    public KeycloakException(MessageCode messageCode) {
        super(messageCode);
    }
}
