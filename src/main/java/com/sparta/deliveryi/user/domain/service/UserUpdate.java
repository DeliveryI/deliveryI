package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import jakarta.validation.Valid;

public interface UserUpdate {
    User updateUserInfo(UserId userId, @Valid UserInfoUpdateRequest updateRequest);
    User updateUserRole(UserId userId, UserRole userRole, String updatedBy);
}
