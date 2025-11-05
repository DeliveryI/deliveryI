package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserModify {
    void modifyUserInfo(UUID keycloakId, @Valid UserInfoUpdateRequest updateRequest);
    void modifyUserRole(UUID keycloakId, UUID userId, UserRole role);
}
