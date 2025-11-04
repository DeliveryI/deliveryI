package com.sparta.deliveryi.user.domain.dto;

import com.sparta.deliveryi.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank String keycloakId,
        @NotBlank @Size(min=4, max=10) String username,
        @NotBlank UserRole role,
        @NotBlank @Size(min=1, max=20) String nickname,
        @NotBlank String phoneNumber,
        String currentAddress
) {}
