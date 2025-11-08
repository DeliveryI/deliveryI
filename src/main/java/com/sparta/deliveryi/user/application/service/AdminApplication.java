package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.presentation.dto.AdminUserResponse;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminApplication {
    AdminUserResponse getUserById(UUID keycloakId, UUID userId);
    Page<AdminUserResponse> search(UUID keycloakId, UserSearchRequest search, Pageable pageable);
    void updateRole(UUID keycloakId, UUID userId, UserRole role);
    void delete(UUID keycloakId, UUID userId);
}
