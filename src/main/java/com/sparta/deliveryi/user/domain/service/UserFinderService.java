package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFinderService implements UserFinder {

    private final UserRepository userRepository;

    @Override
    public User getById(UserId userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));

    }

    @Override
    public Page<User> search(UserSearchRequest search, Pageable pageable) {
        return userRepository.search(search, pageable);
    }

    @Override
    public User getByKeycloakId(KeycloakId keycloakId) {
        return userRepository.findByKeycloakId(keycloakId).orElseThrow(() -> new UserException(UserMessageCode.USER_NOT_FOUND));
    }
}
