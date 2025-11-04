package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AdminUserResponse;
import com.sparta.deliveryi.user.application.dto.MyInfoResponse;
import com.sparta.deliveryi.user.application.dto.UserResponse;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserQuery {
    MyInfoResponse getMyInfo(UUID keycloakId);
    UserResponse getUserById(UUID userId);

    AdminUserResponse getUserForAdminById(UUID keycloakId, UUID userId);

    Page<AdminUserResponse> searchUsersForAdminById(UUID keycloakId, UserSearchRequest search, Pageable pageable);
}