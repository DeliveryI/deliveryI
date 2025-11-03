package com.sparta.deliveryi.user.service;

import com.sparta.deliveryi.user.TestSecurityConfig;
import com.sparta.deliveryi.user.UserFixture;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.service.UserRegister;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Import(TestSecurityConfig.class)
@Transactional
public class UserRegisterTest {

    @Autowired
    private UserRegister userRegister;

    @Test
    void register() {
        UserRegisterRequest registerRequest = UserFixture.createUserRegisterRequest();

        User user = userRegister.register(registerRequest);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getKeycloakId()).isNotNull();
        assertThat(user.getUsername()).isNotNull();
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.getNickname()).isNotNull();
        assertThat(user.getPhoneNumber()).isNotNull();
    }
}
