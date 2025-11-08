package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.LoginUserInfoResponse;
import com.sparta.deliveryi.user.application.dto.UserInfoResponse;
import com.sparta.deliveryi.user.application.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;

import java.time.Instant;
import java.util.UUID;

public interface UserApplication {
    User register(UserRegisterRequest request);
    UserInfoResponse getUserById(UUID userId);
    LoginUserInfoResponse getMyInfo(UUID keycloakId);
    void updateInfo(UUID keycloakId, UserInfoUpdateRequest request);
    void logout(UUID keycloakId, String token, Instant expiry);
    void delete(UUID keycloakId);
}
