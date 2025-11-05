package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakException;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakMessageCode;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakUpdateService implements AuthUpdate {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    @Override
    public void updateUserRole(KeycloakId keycloakId, UserRole role) {
        try {
            RoleScopeResource resource = keycloak.realm(properties.getRealm())
                    .users().get(keycloakId.toString()).roles().realmLevel();

            // 기존 Role 제거
            resource.remove(resource.listAll());

            // 새 Role 추가
            RoleRepresentation roleRepresentation = keycloak.realm(properties.getRealm())
                    .roles().get(role.toString()).toRepresentation();

            resource.add(List.of(roleRepresentation));

        } catch (NotFoundException e) {
            String message = e.getMessage() != null ? e.getMessage() : "";

            if (message.contains("users")) {
                throw new KeycloakException(KeycloakMessageCode.USER_NOT_FOUND);
            } else if (message.contains("roles")) {
                throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, role.toString() + "이(가) Keycloak에 존재하지 않습니다.", e);
            }

            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, e);

        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, e);
        }
    }

}
