package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.User;
import jakarta.validation.Valid;

public interface UserRegister {
    User register(@Valid UserRegisterRequest userRegisterRequest);
}
