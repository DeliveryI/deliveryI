package com.sparta.deliveryi.user.application.dto;

import com.sparta.deliveryi.user.domain.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse (
    UUID userId,
    String username,
    String nickname,
    UserRole role,
    LocalDateTime createdAt,
    String createdBy
){}
