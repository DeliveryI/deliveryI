package com.sparta.deliveryi.user.domain.dto;

public record UserInfoUpdateRequest(
    String nickname,
    String userPhone,
    String currentAddress
) {}
