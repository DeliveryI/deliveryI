package com.sparta.deliveryi.user.application.dto;

import com.sparta.deliveryi.user.domain.UserRole;

public record UserSearchRequest(
        String username,
        String nickname,
        UserRole role
) {}
