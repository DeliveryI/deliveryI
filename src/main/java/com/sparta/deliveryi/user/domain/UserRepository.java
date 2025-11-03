package com.sparta.deliveryi.user.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, UserId> {
    User save(User user);

    Optional<User> findById(UserId userId);
    List<User> findAll();
}
