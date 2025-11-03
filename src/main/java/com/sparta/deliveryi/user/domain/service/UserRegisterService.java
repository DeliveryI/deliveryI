package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRepository;
import com.sparta.deliveryi.user.domain.dto.UserRegisterRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class UserRegisterService implements UserRegister {

    private final UserRepository userRepository;

    @Override
    public User register(@Valid UserRegisterRequest userRegisterRequest) {
        User user = User.register(userRegisterRequest);

        userRepository.save(user);

        return user;
    }
}
