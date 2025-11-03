package com.sparta.deliveryi.user.application.service;

public interface UserRoleService {
    boolean isUserRole(UserId userId, UserRole role);

    UserRole getUserRole(UserId userId);
    void updateUserRole(UserId userId, UserRole role);
}
