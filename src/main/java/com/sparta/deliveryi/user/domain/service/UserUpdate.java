package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;

public interface UserUpdate {
    User updateUserInfo(UserId userId, UserInfoUpdateRequest updateRequest);
    User updateUserRole(UserId userId, UserRole userRole);
}
