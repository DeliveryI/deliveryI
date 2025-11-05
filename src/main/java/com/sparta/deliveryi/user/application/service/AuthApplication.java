package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AuthUser;
import com.sparta.deliveryi.user.domain.UserRole;

import java.util.List;
import java.util.UUID;

public interface AuthApplication {
    AuthUser getUserById(UUID keycloakId);
    List<AuthUser> getUsers();
    void updateRole(UUID keycloakId, UserRole role);
    void logout(UUID keycloakId);
    void delete(UUID keycloakId);
}
