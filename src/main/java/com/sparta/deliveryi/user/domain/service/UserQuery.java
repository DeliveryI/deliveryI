package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQuery {
    // userId를 통한 회원 조회
    User getUserById(UserId userId);

    // 로그인한 회원 조회
    User getUserByKeycloakId(KeycloakId keycloakId);

    // 모든 회원 조회 (페이징, 정렬)
    Page<User> search(UserSearchRequest search,  Pageable pageable);

}
