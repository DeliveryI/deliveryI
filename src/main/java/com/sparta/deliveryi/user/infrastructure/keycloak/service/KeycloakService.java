package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.application.service.AuthApplication;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.application.dto.AuthUser;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeycloakService implements AuthApplication {

    private final KeycloakProvider provider;


    @Override
    public AuthUser getUserById(UUID keycloakId) {
        UserRepresentation user = provider.getUserById(keycloakId.toString());
        UserRole role = provider.getRoleById(keycloakId.toString());

        return AuthUser.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .role(role)
                .build();
    }

    @Override
    public List<AuthUser> getUsers() {
        List<UserRepresentation> users = provider.getUsersResource().list();

        return users.stream()
                .map(user -> {
                    UserRole role = provider.getRoleById(user.getId());
                    return AuthUser.builder()
                            .id(UUID.fromString(user.getId()))
                            .username(user.getUsername())
                            .role(role)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateRole(UUID keycloakId, UserRole role) {
        RoleScopeResource resource = provider.getRoleScopeResourceById(keycloakId.toString());

        // 기존 Role 제거
        resource.remove(resource.listAll());

        // 새 Role 추가
        RoleRepresentation roleRepresentation = provider.toRoleRepresentation(role);

        resource.add(List.of(roleRepresentation));
    }

    @Override
    public void logout(UUID keycloakId) {
        provider.getUserResourceById(keycloakId.toString()).logout();
    }

    @Override
    public void delete(UUID keycloakId) {
        try(Response response = provider.getUsersResource().delete(keycloakId.toString())) {
            provider.checkResponse(response, Response.Status.NO_CONTENT.getStatusCode(), "회원 삭제에 실패하였습니다.");
        }

    }
}

