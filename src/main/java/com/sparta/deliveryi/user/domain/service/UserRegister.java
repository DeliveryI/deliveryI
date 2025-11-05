package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import jakarta.validation.Valid;

public interface UserRegister {
    User register(@Valid UserCreateRequest request);
}
