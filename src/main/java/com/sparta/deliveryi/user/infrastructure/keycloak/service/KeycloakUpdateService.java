package com.sparta.deliveryi.user.infrastructure.keycloak.service;

import com.sparta.deliveryi.user.application.service.AuthUpdate;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakUpdateService implements AuthUpdate {

    private final KeycloakProvider provider;

    @Override
    public void updateUserRole(UUID keycloakId, UserRole role) {
        RoleScopeResource resource = provider.getRoleById(keycloakId.toString());

        // 기존 Role 제거
        resource.remove(resource.listAll());

        // 새 Role 추가
        RoleRepresentation roleRepresentation = provider.toRoleRepresentation(role);

        resource.add(List.of(roleRepresentation));
    }
}
