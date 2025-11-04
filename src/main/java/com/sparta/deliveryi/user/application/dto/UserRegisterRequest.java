package com.sparta.deliveryi.user.application.dto;

import com.sparta.deliveryi.user.presentation.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegisterRequest(
        @NotBlank @Size(min=4, max=10) String username,
        @NotBlank @ValidPassword @Size(min=8, max=15) String password,
        @NotBlank @Size(min=1, max=20) String nickname,
        @NotBlank String userPhone,
        String currentAddress
) {}