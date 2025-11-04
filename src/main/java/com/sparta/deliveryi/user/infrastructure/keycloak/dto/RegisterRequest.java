package com.sparta.deliveryi.user.infrastructure.keycloak.dto;

import com.sparta.deliveryi.user.presentation.annotation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotNull @Size()String username,
    @NotNull @ValidPassword String password
) {}
