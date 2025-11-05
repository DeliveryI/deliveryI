package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.UserRole;

public interface AuthUpdateService {
    void updateUserRole(KeycloakId keycloakId, UserRole role);
}
