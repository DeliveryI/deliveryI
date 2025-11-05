package com.sparta.deliveryi.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
