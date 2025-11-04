package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.AdminUserResponse;
import com.sparta.deliveryi.user.application.dto.MyInfoResponse;
import com.sparta.deliveryi.user.application.dto.UserResponse;
import com.sparta.deliveryi.user.domain.service.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService implements UserQuery {

    private final UserFinder userFinder;

    @Override
    public MyInfoResponse getMyInfo(UUID id) {
        return null;
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return null;
    }

    @Override
    public AdminUserResponse getUserForAdminById(UUID id) {
        return null;
    }

    @Override
    public List<AdminUserResponse> getUsersForAdminById() {
        return List.of();
    }
}
