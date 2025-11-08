package com.sparta.deliveryi.user.presentation.dto;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AdminUserResponse(
        UUID userId,
        String username,
        String nickname,
        UserRole role,
        String UserPhone,
        String currentAddress,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {
    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(
                user.getId().toUuid(),
                user.getUsername(),
                user.getNickname(),
                user.getRole(),
                user.getUserPhone().formatted(),
                user.getCurrentAddress(),
                user.getCreatedAt(),
                user.getCreatedBy(),
                user.getUpdatedAt(),
                user.getUpdatedBy(),
                user.getDeletedAt(),
                user.getDeletedBy()
        );
    }
}
