package com.sparta.deliveryi.user.presentation.dto;

import com.sparta.deliveryi.user.presentation.annotation.ValidPassword;
import com.sparta.deliveryi.user.presentation.annotation.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupReqeust(
        @NotBlank @Size(min=4, max=10) @Pattern(regexp = "^[a-zA-Z0-9]+$") String username,
        @NotBlank @Size(min=8, max=15) @ValidPassword String password,
        @NotBlank String confirmPassword,
        @NotBlank @Size(min=1, max=20) String nickname,
        @NotBlank @ValidPhone String userPhone,
        String currentAddress
) {}
