package com.sparta.deliveryi.user.application.dto;

import com.sparta.deliveryi.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthUser(
        @NotNull UUID id,
        @NotBlank @Size(min=4, max=10) String username,
        @NotNull UserRole role
) {}