package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.service.UserFinder;
import com.sparta.deliveryi.user.infrastructure.keycloak.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserApplicationService implements UserApplication {

    private final AuthService keycloakService;
    private final UserFinder userFiner;
    // private final UserRolePolicy userRolePolicy;

    @Override
    public void logout(UUID keycloakId) {
        keycloakService.logout(KeycloakId.of(keycloakId));
    }

    @Override
    public void delete(UUID keycloakId) {
        // UserId 조회

        // Application DB Soft 삭제

        // Keycloak 삭제
        keycloakService.delete(KeycloakId.of(keycloakId));
    }

    @Override
    public void deleteForce(UUID keycloakId, UUID userId) {
        // loginUser 조회 및 역할 검증

        // UserId 조회

        // Application DB Soft 삭제 -> deletedBy 수동 업데이트

        // Keycloak 삭제
    }
}
