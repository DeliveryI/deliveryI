package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import com.sparta.deliveryi.user.application.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRolePolicyService implements UserRolePolicy {

    private final UserQuery userQuery;
    private final AuthApplication authApplication;

    @Override
    public boolean isAdmin(UUID userId) {
        return isUserRole(userId, List.of(UserRole.MASTER, UserRole.MANAGER));
    }

    @Override
    public boolean isMaster(UUID userId) { return isUserRole(userId, List.of(UserRole.MASTER)); }

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
        KeycloakId keycloakId = userQuery.getUserById(UserId.of(userId)).getKeycloakId();
        // Keycloak 서버에 저장된 역할 조회
        AuthUser user = authApplication.getUserById(keycloakId.toUuid());

        return roles.contains(user.role());
    }
}
