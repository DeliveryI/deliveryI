package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.service.UserFinder;
import com.sparta.deliveryi.user.infrastructure.keycloak.dto.KeycloakUser;
import com.sparta.deliveryi.user.infrastructure.keycloak.service.AuthFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRolePolicyService implements UserRolePolicy {

    private final UserFinder userFinder;
    private final AuthFinder authFinder;

    @Override
    public boolean isAdmin(UUID userId) {
        return isUserRole(userId, List.of(UserRole.MASTER, UserRole.MANAGER));
    }

    @Override
    public boolean isCustomer(UUID userId) {
        return isUserRole(userId, List.of(UserRole.CUSTOMER));
    }

    @Override
    public boolean isOwner(UUID userId) {
        return isUserRole(userId, List.of(UserRole.OWNER));
    }

    private boolean isUserRole(UUID userId, List<UserRole> roles) {
        // 로그인한 회원정보 DB에서 조회
        KeycloakId keycloakId = userFinder.getById(UserId.of(userId)).getKeycloakId();
        // Keycloak 서버에 저장된 역할 조회
        KeycloakUser user = authFinder.find(keycloakId);

        return roles.contains(user.role());
    }
}
