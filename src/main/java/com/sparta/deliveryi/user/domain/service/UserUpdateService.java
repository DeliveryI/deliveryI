package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.*;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class UserUpdateService implements UserUpdate {

    private final UserRepository userRepository;

    @Override
    public User updateUserInfo(UserId userId, @Valid UserInfoUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

        user.updateInfo(updateRequest);

        return user;
    }

    @Override
    public User updateUserRole(UserId userId, UserRole userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

        user.updateRole(userRole);

        return user;
    }
}
