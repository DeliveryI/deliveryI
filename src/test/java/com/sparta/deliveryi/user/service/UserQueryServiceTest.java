package com.sparta.deliveryi.user.service;

import com.sparta.deliveryi.user.TestSecurityConfig;
import com.sparta.deliveryi.user.UserFixture;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.service.UserQueryService;
import com.sparta.deliveryi.user.domain.service.UserRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(TestSecurityConfig.class)
@Transactional
public class UserQueryServiceTest {

    @Autowired
    private UserRegister userRegister;

    @Autowired
    private UserQueryService userQueryService;

    List<User> users;
    int size = 10;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        for (int i=0; i<size; i++) {
            UserRegisterRequest registerRequest = UserFixture.createUserRegisterRequest(i);
            User user = userRegister.register(registerRequest);
            users.add(user);
        }
    }

    @Test
    void findWithValidId() {
        User targetUser = users.getFirst();
        User result = userQueryService.find(targetUser.getId());
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void findWithInvalidId() {
        UserId invalidUserId = UserId.of(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> {
           userQueryService.find(invalidUserId);
        });
    }

    @Test
    void findAll() {
        List<User> result = userQueryService.findAll();

        assertThat(users.size()).isEqualTo(size);

        List<UserId> userIds = users.stream().map(User::getId).toList();
        List<UserId> resultIds = result.stream().map(User::getId).toList();

        assertThat(resultIds).containsAll(userIds);
    }

}
