package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.sparta.deliveryi.user.UserFixture.createUser;
import static com.sparta.deliveryi.user.UserFixture.createUserInfoUpdateRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = createUser();
        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    @Test
    void register() {
        assertThat(user.getUsername()).isEqualTo("user001");
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.getNickname()).isEqualTo("고객001");
        assertThat(user.getPhoneNumber().toString()).isEqualTo("01012345678");
        assertThat(user.getCurrentAddress()).isEqualTo("강남구 도곡로 112");
    }

    @Test
    void updateInfo() {
        UserInfoUpdateRequest updateRequest = createUserInfoUpdateRequest();

        user.updateInfo(updateRequest);

        assertThat(user.getUsername()).isEqualTo("user001");
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.getNickname()).isEqualTo("고객001");
        assertThat(user.getPhoneNumber().toString()).isEqualTo("01012345678");
        assertThat(user.getCurrentAddress()).isEqualTo("");
    }

    @Test
    void updateRole() {
        user.updateRole(UserRole.MANAGER);

        assertThat(user.getRole()).isEqualTo(UserRole.MANAGER);
    }
}
