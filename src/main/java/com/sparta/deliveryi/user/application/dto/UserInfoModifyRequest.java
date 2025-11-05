package com.sparta.deliveryi.user.application.dto;

import jakarta.validation.constraints.Size;

public record UserInfoModifyRequest(
        @Size(min=1, max=20) String nickname,
        String userPhone,
        String currentAddress
) {}
