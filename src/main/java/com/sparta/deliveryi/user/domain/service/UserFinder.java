package com.sparta.deliveryi.user.domain.service;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;

import java.util.List;

public interface UserFinder {
    User find(UserId userId);
    List<User> findAll();
}
