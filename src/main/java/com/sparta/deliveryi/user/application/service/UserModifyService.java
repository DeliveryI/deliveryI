package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.*;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import com.sparta.deliveryi.user.domain.service.UserFinder;
import com.sparta.deliveryi.user.domain.service.UserUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class UserModifyService implements UserModify {

    private final UserFinder userFinder;
    private final UserUpdate userUpdate;
    private final UserRolePolicy userRolePolicy;

    @Override
    public void modifyUserInfo(UUID keycloakId, @Valid UserInfoUpdateRequest updateRequest) {
        User user = userFinder.getByKeycloakId(KeycloakId.of(keycloakId));

        userUpdate.updateUserInfo(user.getId(), updateRequest);
    }

    @Override
    public void modifyUserRole(UUID keycloakId, UUID userId, UserRole role) {
        User loginUser = userFinder.getByKeycloakId(KeycloakId.of(keycloakId));

        if (!userRolePolicy.isAdmin(loginUser.getId().toUuid())) {
            throw new UserException(UserMessageCode.ACCESS_FORBIDDEN);
        }

        User user = userFinder.get(UserId.of(userId));

        userUpdate.updateUserRole(user.getId(), role);
    }
}
