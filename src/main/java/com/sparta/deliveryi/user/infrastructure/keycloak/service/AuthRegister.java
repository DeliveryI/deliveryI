package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakUser;
import com.sparta.deliveryi.user.infrastructure.keycloak.dto.KeycloakRegisterRequest;
import jakarta.validation.Valid;

public interface AuthRegister {
    KeycloakUser register(@Valid KeycloakRegisterRequest request);
}
