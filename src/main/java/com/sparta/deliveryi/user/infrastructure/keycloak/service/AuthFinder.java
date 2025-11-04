package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakUser;

public interface AuthFinder {
    KeycloakUser find(KeycloakId keycloakId);
}
