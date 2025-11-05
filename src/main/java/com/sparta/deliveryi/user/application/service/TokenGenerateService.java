package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.TokenInfo;

public interface TokenGenerateService {
    TokenInfo generate(String username, String password);
}
