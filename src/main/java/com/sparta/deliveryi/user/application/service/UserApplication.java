package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.domain.User;

import java.util.UUID;

public interface UserApplication {
    UUID getUserIdByKeycloakId(UUID keycloakId);
    User getUserByKeycloakId(UUID keycloakId);
    User getUserByUserId(UUID userId);
}
