package com.sparta.deliveryi.user;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakUser;

import java.util.UUID;

public class UserFixture {

    public static User createUser() {
        UserCreateRequest userRegister = createUserCreateRequest();

        return User.create(userRegister);
    }

    public static UserCreateRequest createUserCreateRequest() {
        return createUserCreateRequest(1);
    }

    public static UserCreateRequest createUserCreateRequest(Integer number) {
        KeycloakUser keycloakUser = KeycloakUser.builder()
                .id(UUID.randomUUID())
                .username("user00" + number)
                .role(UserRole.CUSTOMER)
                .build();

        return new UserCreateRequest(
                keycloakUser,
                "고객00" + number,
                "010-1234-5678",
                "강남구 도곡로 112"
        );
    }

    public static UserInfoUpdateRequest createUserInfoUpdateRequest() {
        return new UserInfoUpdateRequest(
                "고객001",
                "010-1234-5678",
                ""
        );
    }

}
