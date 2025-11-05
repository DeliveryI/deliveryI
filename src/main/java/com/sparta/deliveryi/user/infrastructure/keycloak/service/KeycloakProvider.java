package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakException;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakMessageCode;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakProvider {

    private final Keycloak keycloak;
    private final KeycloakProperties properties;

    UsersResource getUsersResource() {
        return keycloak.realm(properties.getRealm()).users();
    }

    UserResource getUserResourceById(String id) {
        try {
            return keycloak.realm(properties.getRealm()).users().get(id);
        } catch (NotFoundException e) {
            throw new KeycloakException(KeycloakMessageCode.USER_NOT_FOUND);
        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, e);
        }
    }

    RoleScopeResource getRoleById(String id) {
        return getUserResourceById(id).roles().realmLevel();
    }

    List<UserRepresentation> findUsers() {
        try {
            return keycloak.realm(properties.getRealm())
                    .users()
                    .list();
        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, e);
        }
    }

    UserRepresentation getUserById(String id) {
        return getUserResourceById(id).toRepresentation();
    }

    UserRole toUserRole(UserRepresentation user) {
        try {
            String role = user.getAttributes().get("role").getFirst();
            return UserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new KeycloakException(
                    KeycloakMessageCode.INTERNAL_FAILED,
                    "Keycloak에 존재하지 않거나 올바르지 않은 역할입니다.",
                    e
            );
        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, e);
        }
    }

    RoleRepresentation toRoleRepresentation(UserRole role) {
        RoleRepresentation roleRepresentation = keycloak.realm(properties.getRealm())
                .roles()
                .get(role.getAuthority())
                .toRepresentation();

        if (roleRepresentation == null) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, role + "이(가) Keycloak에 존재하지 않습니다.", new IllegalStateException());
        }

        return roleRepresentation;
    }

    void checkResponse(Response response, int expectedStatus, String message) {
        if (response.getStatus() != expectedStatus) {
            String body = response.hasEntity() ? response.readEntity(String.class) : null;
            int status = response.getStatus();
            String reason = response.getStatusInfo().getReasonPhrase();

            throw new KeycloakException(
                    KeycloakMessageCode.INTERNAL_FAILED,
                    String.format("%s (%d %s): %s", message, status, reason, body)
            );
        }
    }
}
