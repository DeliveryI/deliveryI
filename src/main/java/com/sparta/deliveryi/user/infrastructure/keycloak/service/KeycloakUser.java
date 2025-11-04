package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.infrastructure.keycloak.dto.RegisterRequest;
import jakarta.validation.Valid;

public interface KeycloakUser {
    KeycloakId register(@Valid RegisterRequest request);
}
