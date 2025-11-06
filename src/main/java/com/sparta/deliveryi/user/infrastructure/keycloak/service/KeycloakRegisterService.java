package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.application.dto.AuthUser;
import com.sparta.deliveryi.user.application.service.AuthRegister;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakRegisterService implements AuthRegister {

    private final KeycloakProvider provider;

    @Override
    public AuthUser register(String username, String password) {
        String keycloakId;

        // 사용자 생성
        try (Response response = createUser(username)) {
            provider.checkResponse(response, Response.Status.CREATED.getStatusCode(), "Keycloak 회원 생성 실패");
            keycloakId = CreatedResponseUtil.getCreatedId(response);
        }

        // 비밀번호 설정
        setPassword(keycloakId, password);

        // 기본 Role 부여
        UserRole role = setDefaultRole(keycloakId);

        return AuthUser.builder()
                .id(UUID.fromString(keycloakId))
                .username(username)
                .role(role)
                .build();
    }

    private Response createUser(String username) {
        UsersResource resource = provider.getUsersResource();

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);

        return resource.create(user);
    }

    private void setPassword(String keycloakId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        provider.getUserResourceById(keycloakId).resetPassword(credential);
    }

    private UserRole setDefaultRole(String keycloakId) {
        UserRole defaultRole = UserRole.CUSTOMER;

        RoleRepresentation role = provider.toRoleRepresentation(defaultRole);
        provider.getRoleScopeResourceById(keycloakId).add(List.of(role));

        return UserRole.CUSTOMER;
    }
}
