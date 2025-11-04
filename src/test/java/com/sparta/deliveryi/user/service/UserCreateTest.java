package com.sparta.deliveryi.user.service;

import com.sparta.deliveryi.user.UserFixture;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import com.sparta.deliveryi.user.domain.service.UserCreate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class UserCreateTest {

    @Autowired
    private UserCreate userCreate;

    @Test
    void create() {
        UserCreateRequest registerRequest = UserFixture.createUserCreateRequest();

        User user = userCreate.create(registerRequest);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getKeycloakId()).isNotNull();
        assertThat(user.getUsername()).isNotNull();
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.getNickname()).isNotNull();
        assertThat(user.getUserPhone()).isNotNull();
    }
}
