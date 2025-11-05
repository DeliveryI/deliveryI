package com.sparta.deliveryi.user.domain.dto;

import jakarta.validation.constraints.Size;

public record UserInfoUpdateRequest(
    @Size(min=1, max=20) String nickname,
    String userPhone,
    String currentAddress
) {}
