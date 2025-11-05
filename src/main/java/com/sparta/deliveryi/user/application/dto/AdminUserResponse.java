package com.sparta.deliveryi.user.application.dto;

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
        String currentAddress,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy
) {}
