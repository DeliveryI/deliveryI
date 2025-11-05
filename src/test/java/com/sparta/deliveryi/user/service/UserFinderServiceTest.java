package com.sparta.deliveryi.user.service;

import com.sparta.deliveryi.user.UserFixture;
import com.sparta.deliveryi.user.application.dto.UserSearchRequest;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import com.sparta.deliveryi.user.domain.service.UserCreate;
import com.sparta.deliveryi.user.domain.service.UserFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserFinderServiceTest {

    @Autowired
    private UserCreate userCreate;

    @Autowired
    private UserFinder userFinder;

    List<User> users;
    int size = 10;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        for (int i=0; i<size; i++) {
            UserCreateRequest registerRequest = UserFixture.createUserCreateRequest(i);
            User user = userCreate.create(registerRequest);
            users.add(user);
        }
    }

    @Test
    void findWithValidId() {
        User targetUser = users.getFirst();
        User result = userFinder.find(targetUser.getId())
                .orElseThrow(AssertionError::new);
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void findWithInvalidId() {
        UserId invalidUserId = UserId.of(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> {
           userFinder.find(invalidUserId);
        });
    }

    @Test
    void search() {
        UserSearchRequest search = new UserSearchRequest(null, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        Page<User> result = userFinder.search(search, pageable);

        assertThat(result.getTotalElements()).isEqualTo(users.size());

        List<UserId> userIds = users.stream().map(User::getId).toList();
        List<UserId> resultIds = result.stream().map(User::getId).toList();

        assertThat(resultIds).containsAll(userIds);
    }

}
