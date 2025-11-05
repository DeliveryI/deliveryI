package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserFinder {
    Optional<User> find(UserId userId);
    Optional<User> findByKeycloakId(KeycloakId keycloakId);
    Page<User> search(UserSearchRequest search,  Pageable pageable);
}
