package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakException;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakMessageCode;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakUser;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeycloakQueryService implements AuthFinder {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    @Override
    public KeycloakUser find(KeycloakId keycloakId) {

        try {
            UserRepresentation user = keycloak.realm(properties.getRealm())
                    .users()
                    .get(keycloakId.toString())
                    .toRepresentation();

            return KeycloakUser.builder()
                    .id(UUID.fromString(user.getId()))
                    .username(user.getUsername())
                    .role(UserRole.valueOf(user.getAttributes().get("role").getFirst()))
                    .build();
        } catch (NotFoundException e) {
            throw new KeycloakException(KeycloakMessageCode.NOT_FOUND);
        }
    }
}
