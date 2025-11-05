package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.*;
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
    private final UserFinder userFinder;
    private final UserRolePolicy userRolePolicy;

    @Override
    public void logout(UUID keycloakId) {
        keycloakService.logout(KeycloakId.of(keycloakId));
    }

    @Override
    public void delete(UUID keycloakId) {
        // UserId 조회
        User user = userFinder.getByKeycloakId(KeycloakId.of(keycloakId));

        deleteUser(user);
    }

    @Override
    public void deleteForce(UUID keycloakId, UUID userId) {
        // loginUser 조회 및 역할 검증
        User loginUser = userFinder.getByKeycloakId(KeycloakId.of(keycloakId));
        if (!userRolePolicy.isAdmin(loginUser.getId().toUuid())) {
            throw new UserException(UserMessageCode.ACCESS_FORBIDDEN);
        }

        // UserId 조회
        User user = userFinder.getById(UserId.of(userId));

        deleteUser(user);
    }

    private void deleteUser(User user) {
        KeycloakId keycloakId = user.getKeycloakId();

        // Token 무효화
        keycloakService.logout(keycloakId);

        // Application DB Soft 삭제
        user.delete();

        // Keycloak 삭제
        keycloakService.delete(keycloakId);
    }
}
