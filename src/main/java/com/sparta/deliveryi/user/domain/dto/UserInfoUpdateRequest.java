package com.sparta.deliveryi.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInfoUpdateRequest(
    @NotBlank @Size(min=1, max=20) String nickname,
    @NotBlank String userPhone,
    @NotBlank String currentAddress
) {}
