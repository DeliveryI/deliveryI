package com.sparta.deliveryi.user.service;

import com.sparta.deliveryi.user.TestSecurityConfig;
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

import java.util.List;
import java.util.UUID;

import static com.sparta.deliveryi.user.UserFixture.createUserRegisterRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(TestSecurityConfig.class)
@Transactional
public class UserQueryServiceTest {

    @Autowired
    private UserRegister userRegister;

    @Autowired
    private UserQueryService userQueryService;

    User savedUser;

    @BeforeEach
    void setUp() {
        UserRegisterRequest registerRequest = createUserRegisterRequest();
        savedUser = userRegister.register(registerRequest);
    }

    @Test
    void findSuccess() {
        User user = userQueryService.find(savedUser.getId());

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void findFailure() {
        UserId invalidUserId = UserId.of(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> {
           userQueryService.find(invalidUserId);
        });
    }

    @Test
    void findAll() {
        List<User> users = userQueryService.findAll();

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getId()).isEqualTo(savedUser.getId());
    }

}
