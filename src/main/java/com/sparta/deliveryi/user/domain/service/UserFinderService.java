package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFinderService implements UserFinder {

    private final UserRepository userRepository;

    @Override
    public User find(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다. id: " + userId));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
