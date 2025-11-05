package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;

public interface AuthService {
    void logout(KeycloakId keycloakId);
    void delete(KeycloakId keycloakId);
}
