package com.sparta.deliveryi.user.application.service;

import java.util.UUID;

public interface UserApplication {
    void logout(UUID keycloakId);
    void delete(UUID keycloakId);
    void deleteForce(UUID keycloakId, UUID userId);
}
