package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.KeycloakId;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFinderService implements UserFinder {

    private final UserRepository userRepository;

    @Override
    public Optional<User> find(UserId userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByKeycloakId(KeycloakId keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public Page<User> search(UserSearchRequest search, Pageable pageable) {
        return userRepository.search(search, pageable);
    }
}
