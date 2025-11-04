package com.sparta.deliveryi.user;

import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;

import java.util.UUID;

public class UserFixture {

    public static User createUser() {
        UserRegisterRequest userRegisterRequest = createUserRegisterRequest();

        return User.register(userRegisterRequest);
    }

    public static UserRegisterRequest createUserRegisterRequest() {
        return createUserRegisterRequest(1);
    }

    public static UserRegisterRequest createUserRegisterRequest(Integer number) {
        return new UserRegisterRequest(
                UUID.randomUUID().toString(),
                "user00" + number,
                UserRole.CUSTOMER,
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
