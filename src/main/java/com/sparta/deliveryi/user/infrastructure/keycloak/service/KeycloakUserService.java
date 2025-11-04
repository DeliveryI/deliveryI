package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import com.sparta.deliveryi.user.infrastructure.keycloak.dto.RegisterRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakUserService implements KeycloakUser {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    @Override
    public KeycloakId register(@Valid RegisterRequest request) {
        // keycloak에 사용자 생성
        UsersResource resource = keycloak.realm(properties.getRealm()).users();

        // 사용자 생성
        String userId;
        try (Response response = createUser(resource, request)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new HttpClientErrorException(
                        HttpStatus.valueOf(response.getStatus()),
                        response.getStatusInfo().getReasonPhrase(),
                        response.readEntity(String.class).getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8
                );
            }

            userId = CreatedResponseUtil.getCreatedId(response);
        }

        // 비밀번호 설정
        setPassword(resource, userId, request.password());

        // 기본 Role 부여
        setDefaultRole(resource, userId);

        return KeycloakId.of(userId);

    }

    private Response createUser(UsersResource resource, RegisterRequest request) {
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

    private void setDefaultRole(UsersResource resource, String userId) {
        RoleRepresentation role = keycloak.realm(properties.getRealm())
                .roles()
                .get(UserRole.CUSTOMER.getAuthority())
                .toRepresentation();


        if (role == null) {
            throw new IllegalStateException(UserRole.CUSTOMER.getAuthority() + "가 Keycloak에 존재하지 않습니다.");
        }

        resource.get(userId).roles().realmLevel().add(List.of(role));
    }
}
