package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRepository;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class UserCreateService implements UserCreate {

    private final UserRepository userRepository;

    @Override
    public User create(@Valid UserCreateRequest request) {
        User user = User.create(request);

        userRepository.save(user);

        return user;
    }
}
