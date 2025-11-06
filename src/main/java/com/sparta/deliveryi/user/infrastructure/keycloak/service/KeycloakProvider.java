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

    // 회원 리소스 반환
    UsersResource getUsersResource() {
        return keycloak.realm(properties.getRealm()).users();
    }

    // 특정 회원 리소스 반환 -> 회원에 대한 다양한 설정 가능
    UserResource getUserResourceById(String id) {
        try {
            return keycloak.realm(properties.getRealm()).users().get(id);
        } catch (NotFoundException e) {
            throw new KeycloakException(KeycloakMessageCode.USER_NOT_FOUND);
        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, "회원 리소스 조회 중 오류가 발생했습니다.", e);
        }
    }

    // 특정 회원의 역할스코프 반환
    RoleScopeResource getRoleScopeResourceById(String id) {
        try {
            return getUserResourceById(id).roles().realmLevel();
        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, "회원의 역할 스코프 조회 중 오류가 발생했습니다.", e);
        }
    }

    // 특정 회원의 역할 목록 반환
    UserRole getRoleById(String id) {
        List<RoleRepresentation> roles = getUserResourceById(id).roles().realmLevel().listAll();

        if (roles == null || roles.isEmpty()) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, "");
        }

        String name = roles.getFirst().getName();
        try {
            return UserRole.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new KeycloakException(
                    KeycloakMessageCode.INTERNAL_FAILED,
                    String.format("Keycloak의 역할 '%s'이(가) UserRole에 존재하지 않습니다.", name),
                    e
            );
        }
    }

    // 특정 회원정보 반환
    UserRepresentation getUserById(String id) {
        try {
            return getUserResourceById(id).toRepresentation();
        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, "회원정보 조회 중 오류가 발생했습니다.", e);
        }

    }

    // UserRole로 Keycloak role 반환
    RoleRepresentation toRoleRepresentation(UserRole role) {
        try {
            RoleRepresentation roleRepresentation = keycloak.realm(properties.getRealm())
                    .roles()
                    .get(role.getAuthority())
                    .toRepresentation();

            if (roleRepresentation == null) {
                throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, role + "이(가) Keycloak에 존재하지 않습니다.", new IllegalStateException());
            }

            return roleRepresentation;
        } catch (Exception e) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, "역할 변환 중 오류가 발생했습니다.", e);
        }
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
