package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, UserId>, UserRepositoryCustom {
    User save(User user);
    Optional<User> findById(UserId userId);
    Optional<User> findByKeycloakId(KeycloakId keycloakId);
    Page<User> search(UserSearchRequest search, Pageable pageable);
}
