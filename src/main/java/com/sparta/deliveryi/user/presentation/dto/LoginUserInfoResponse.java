package com.sparta.deliveryi.user.presentation.dto;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginUserInfoResponse(
        UUID userId,
        String username,
        String nickname,
        UserRole role,
        String userPhone,
        String currentAddress,
        LocalDateTime createdAt,
        String createdBy
) {
    public static LoginUserInfoResponse from(User user) {
        return new LoginUserInfoResponse(
                user.getId().toUuid(),
                user.getUsername(),
                user.getNickname(),
                user.getRole(),
                user.getUserPhone().formatted(),
                user.getCurrentAddress(),
                user.getCreatedAt(),
                user.getCreatedBy()
        );
    }
}