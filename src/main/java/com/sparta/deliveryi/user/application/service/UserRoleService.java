package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.UserRole;

import java.util.UUID;

public interface UserRoleService {
    boolean isAdmin(UUID userId);

    void updateUserRole(UUID userId, UserRole role);
}
