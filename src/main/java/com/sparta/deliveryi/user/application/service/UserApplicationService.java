package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AuthUser;
import com.sparta.deliveryi.user.application.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import com.sparta.deliveryi.user.domain.service.UserRegister;
import com.sparta.deliveryi.user.domain.service.UserUpdate;
import com.sparta.deliveryi.user.security.TokenBlacklist;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserApplicationService implements UserApplication {

    private final TokenBlacklist tokenBlacklist;

    private final AuthApplication authApplication;
    private final AuthRegister authRegister;
    private final UserQuery userQuery;
    private final UserUpdate userUpdate;
    private final UserRegister userRegister;

    @Override
    public User register(UserRegisterRequest request) {
        AuthUser authUser = authRegister.register(request.username(), request.password());

        UserCreateRequest createRequest = UserCreateRequest.builder()
                .authUser(authUser)
                .nickname(request.nickname())
                .userPhone(request.userPhone())
                .currentAddress(request.currentAddress())
                .build();

        return userRegister.register(createRequest);
    }

    @Override
    public User getUserById(UUID userId) {
        return userQuery.getUserById(UserId.of(userId));
    }

    @Override
    public User getLoginUser(UUID keycloakId) {
        return userQuery.getUserByKeycloakId(KeycloakId.of(keycloakId));
    }

    @Override
    public void updateInfo(UUID keycloakId, UserInfoUpdateRequest request) {
        User user = userQuery.getUserByKeycloakId(KeycloakId.of(keycloakId));
        userUpdate.updateInfo(user.getId(), request);
    }

    @Override
    public void logout(UUID keycloakId, String token, Instant expiry) {
        // Keycloak Refresh Token revoke
        authApplication.logout(keycloakId);

        // Add Blacklist
        tokenBlacklist.add(token, expiry);
    }

    @Override
    public void delete(UUID keycloakId) {
        authApplication.logout(keycloakId);
        authApplication.delete(keycloakId);

        User user = userQuery.getUserByKeycloakId(KeycloakId.of(keycloakId));
        user.delete();
    }
}
