package com.sparta.deliveryi.user.domain.dto;

import com.sparta.deliveryi.user.application.dto.AuthUser;
import com.sparta.deliveryi.user.presentation.annotation.ValidPhone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserCreateRequest(
        @Valid AuthUser authUser,
        @NotBlank @Size(min=1, max=20) String nickname,
        @NotBlank @ValidPhone String userPhone,
        String currentAddress
) {}
