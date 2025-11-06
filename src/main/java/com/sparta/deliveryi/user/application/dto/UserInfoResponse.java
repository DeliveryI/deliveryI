package com.sparta.deliveryi.user.application.dto;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserInfoResponse(
        UUID userId,
        String username,
        String nickname,
        UserRole role,
        LocalDateTime createdAt,
        String createdBy
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId().toUuid(),
                user.getUsername(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt(),
                user.getCreatedBy()
        );
    }
}
