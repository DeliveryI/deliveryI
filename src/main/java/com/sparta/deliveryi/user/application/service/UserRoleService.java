package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.UserRole;

public interface UserRoleService {
    boolean isUserRole(UserId userId, UserRole role);

    UserRole getUserRole(UserId userId);
    void updateUserRole(UserId userId, UserRole role);
}
