package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AdminUserResponse;
import com.sparta.deliveryi.user.application.dto.MyInfoResponse;
import com.sparta.deliveryi.user.application.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserQuery {
    MyInfoResponse getMyInfo(UUID id);
    UserResponse getUserById(UUID id);
    AdminUserResponse getUserForAdminById(UUID id);
    List<AdminUserResponse> getUsersForAdminById();
}