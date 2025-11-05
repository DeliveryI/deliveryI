package com.sparta.deliveryi.user.presentation.dto;

import com.sparta.deliveryi.user.domain.UserRole;
import jakarta.validation.constraints.NotNull;

public record UserRoleChangeRequest (
        @NotNull UserRole role
) {}
