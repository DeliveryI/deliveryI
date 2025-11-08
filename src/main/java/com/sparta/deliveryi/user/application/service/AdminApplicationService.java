package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AdminUserResponse;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.*;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import com.sparta.deliveryi.user.domain.service.UserUpdate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminApplicationService implements AdminApplication {

    private final AuthApplication authApplication;
    private final UserQuery userQuery;
    private final UserUpdate userUpdate;
    private final UserRolePolicy userRolePolicy;

    @Override
    public AdminUserResponse getUserById(UUID keycloakId, UUID userId) {
        isLoginAdmin(keycloakId);

        User user = userQuery.getUserById(UserId.of(userId));
        return AdminUserResponse.from(user);
    }

    @Override
    public Page<AdminUserResponse> search(UUID keycloakId, UserSearchRequest search, Pageable pageable) {
        isLoginAdmin(keycloakId);

        return userQuery.search(search, pageable).map(AdminUserResponse::from);
    }

    @Override
    public void updateRole(UUID keycloakId, UUID userId, UserRole role) {
        User loginUser = isLoginAdmin(keycloakId);

        User user = userQuery.getUserById(UserId.of(userId));
        authApplication.updateRole(user.getKeycloakId().toUuid(), role);
        userUpdate.updateRole(UserId.of(userId), role, loginUser.getUpdatedBy());

        authApplication.logout(user.getKeycloakId().toUuid());
    }

    @Override
    public void delete(UUID keycloakId, UUID userId) {
        isLoginAdmin(keycloakId);

        authApplication.logout(keycloakId);
        authApplication.delete(keycloakId);

        User user = userQuery.getUserById(UserId.of(userId));
        user.delete();
    }

    private User isLoginAdmin(UUID keycloakId) {
        User loginUser = userQuery.getUserByKeycloakId(KeycloakId.of(keycloakId));

        if (!userRolePolicy.isAdmin(loginUser.getId().toUuid())) {
            throw new UserException(UserMessageCode.ACCESS_FORBIDDEN);
        }

        return loginUser;
    }
}
