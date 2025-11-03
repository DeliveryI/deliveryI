package com.sparta.deliveryi.user.domain.dto;

public record UserInfoUpdateRequest(
    String nickname,
    String phoneNumber,
    String currentAddress
) {}
