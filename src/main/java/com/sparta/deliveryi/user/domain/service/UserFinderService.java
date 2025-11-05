package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.*;
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
    public Page<User> search(UserSearchRequest search, Pageable pageable) {
        return userRepository.search(search, pageable);
    }

    @Override
    public Optional<User> findByKeycloakId(KeycloakId keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public User get(UserId userId) {
        return find(userId).orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

    }

    @Override
    public User getByKeycloakId(KeycloakId keycloakId) {
        return findByKeycloakId(keycloakId).orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));
    }
}
