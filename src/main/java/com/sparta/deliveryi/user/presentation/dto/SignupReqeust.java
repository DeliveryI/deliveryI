package com.sparta.deliveryi.user.presentation.dto;

import com.sparta.deliveryi.user.presentation.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupReqeust(
        @NotBlank(message = "회원 아이디는 필수 입력입니다.")
        @Size(min=4, max=10, message = "회원 아이디는 4자 이상 10자 이하로 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "사용자 이름은 알파벳(a~z, A~Z)과 숫자(0~9)만 사용 가능합니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        @Size(min=8, max=15, message = "비밀번호는 8자 이상 15자 이하로 입력해주세요.")
        @ValidPassword
        String password,

        @NotBlank(message = "비밀번호 확인은 필수 입력입니다.") String confirmPassword,

        @NotBlank(message = "닉네임은 필수 입력입니다.")
        @Size(min=1, max=20, message = "닉네임은 1자 이상 20자 이히로 입력해주세요.")
        String nickname,

        @NotBlank(message = "전화번호는 필수 입력입니다.") String userPhone,
        String currentAddress
) {}
