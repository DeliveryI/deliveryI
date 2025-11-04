package com.sparta.deliveryi.user.service;

import com.sparta.deliveryi.DeliveryITestConfiguration;
import com.sparta.deliveryi.user.UserFixture;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import com.sparta.deliveryi.user.domain.service.UserCreate;
import com.sparta.deliveryi.user.domain.service.UserQueryService;
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
@Transactional
@Import(DeliveryITestConfiguration.class)
public class UserQueryServiceTest {

    @Autowired
    private UserCreate userCreate;

    @Autowired
    private UserQueryService userQueryService;

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
