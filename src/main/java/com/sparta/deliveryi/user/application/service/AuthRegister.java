package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AuthUser;

public interface AuthRegister {
    AuthUser register(String username, String password);
}
