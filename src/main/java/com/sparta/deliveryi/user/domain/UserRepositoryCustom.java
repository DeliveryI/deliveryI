package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> search(UserSearchRequest search, Pageable pageable);
}
