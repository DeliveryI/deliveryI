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
    Page<User> search(UserSearchRequest search,  Pageable pageable);

    // 로그인한 회원의 정보를 가져옵니다.
    Optional<User> findByKeycloakId(KeycloakId keycloakId);

    User get(UserId userId);
    User getByKeycloakId(KeycloakId keycloakId);
}
