package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AdminUserResponse;
import com.sparta.deliveryi.user.application.dto.MyInfoResponse;
import com.sparta.deliveryi.user.application.dto.UserResponse;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.*;
import com.sparta.deliveryi.user.domain.service.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService implements UserQuery {

    private UserRoleService userRoleService;
    private UserFinder userFinder;

    @Override
    public MyInfoResponse getMyInfo(UUID keycloakId) {
        User user = userFinder.findByKeycloakId(KeycloakId.of(keycloakId))
                .orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

        return MyInfoResponse.builder()
                .userId(user.getId().toUuid())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .userPhone(user.getUserPhone().formatted())
                .currentAddress(user.getCurrentAddress())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .build();
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        User user = userFinder.find(UserId.of(userId))
                .orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

        return UserResponse.builder()
                .userId(user.getId().toUuid())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .build();
    }

    @Override
    public AdminUserResponse getUserForAdminById(UUID keycloakId, UUID userId) {
        User loginUser = userFinder.findByKeycloakId(KeycloakId.of(keycloakId))
                .orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

        if (!userRoleService.isAdmin(loginUser.getId().toUuid())) {
            throw new UserException(UserMessageCode.ACCESS_FORBIDDEN);
        }

        User user = userFinder.find(UserId.of(userId))
                .orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

        return AdminUserResponse.builder()
                .userId(user.getId().toUuid())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .currentAddress(user.getCurrentAddress())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .deletedAt(user.getDeletedAt())
                .deletedBy(user.getDeletedBy())
                .build();
    }

    @Override
    public Page<AdminUserResponse> searchUsersForAdminById(UUID keycloakId, UserSearchRequest search, Pageable pageable) {
        User loginUser = userFinder.findByKeycloakId(KeycloakId.of(keycloakId))
                .orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

        if (!userRoleService.isAdmin(loginUser.getId().toUuid())) {
            throw new UserException(UserMessageCode.ACCESS_FORBIDDEN);
        }

        return userFinder.search(search, pageable)
                .map(user -> AdminUserResponse.builder()
                        .userId(user.getId().toUuid())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .role(user.getRole())
                        .currentAddress(user.getCurrentAddress())
                        .createdAt(user.getCreatedAt())
                        .createdBy(user.getCreatedBy())
                        .updatedAt(user.getUpdatedAt())
                        .updatedBy(user.getUpdatedBy())
                        .deletedAt(user.getDeletedAt())
                        .deletedBy(user.getDeletedBy())
                        .build());
    }
}
