package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakException;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakMessageCode;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import com.sparta.deliveryi.user.infrastructure.keycloak.dto.KeycloakUser;
import com.sparta.deliveryi.user.infrastructure.keycloak.dto.KeycloakRegisterRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
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

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    @Override
    public KeycloakUser register(@Valid KeycloakRegisterRequest request) {
        // keycloak에 사용자 생성
        UsersResource resource = keycloak.realm(properties.getRealm()).users();

        // 사용자 생성
        String keycloakId;
        try (Response response = createUser(resource, request)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                String body = response.hasEntity() ? response.readEntity(String.class) : null;
                int status = response.getStatus();
                String reason = response.getStatusInfo().getReasonPhrase();

                throw new KeycloakException(
                        KeycloakMessageCode.INTERNAL_FAILED,
                        String.format("Keycloak 요청 실패 (%d %s): %s", status, reason, body)
                );
            }

            keycloakId = CreatedResponseUtil.getCreatedId(response);
        }

        // 비밀번호 설정
        setPassword(resource, keycloakId, request.password());

        // 기본 Role 부여
        UserRole defaultRole = setDefaultRole(resource, keycloakId);

        return KeycloakUser.builder()
                .id(UUID.fromString(keycloakId))
                .username(request.username())
                .role(defaultRole)
                .build();

    }

    private Response createUser(UsersResource resource, KeycloakRegisterRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(request.username());

        return resource.create(user);
    }

    private void setPassword(UsersResource resource, String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        resource.get(userId).resetPassword(credential);
    }

    private UserRole setDefaultRole(UsersResource resource, String userId) {
        String defaultRole = UserRole.CUSTOMER.getAuthority();

        RoleRepresentation role = keycloak.realm(properties.getRealm())
                .roles()
                .get(defaultRole)
                .toRepresentation();


        if (role == null) {
            throw new KeycloakException(KeycloakMessageCode.INTERNAL_FAILED, defaultRole + "이(가) Keycloak에 존재하지 않습니다.", new IllegalStateException());
        }

        resource.get(userId).roles().realmLevel().add(List.of(role));

        return UserRole.CUSTOMER;
    }
}
