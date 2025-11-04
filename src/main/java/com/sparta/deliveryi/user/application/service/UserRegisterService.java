package com.sparta.deliveryi.user.application.service;

import com.sparta.deliveryi.user.application.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
import com.sparta.deliveryi.user.domain.service.UserCreate;
import com.sparta.deliveryi.user.infrastructure.keycloak.KeycloakUser;
import com.sparta.deliveryi.user.infrastructure.keycloak.dto.KeycloakRegisterRequest;
import com.sparta.deliveryi.user.infrastructure.keycloak.service.KeycloakRegister;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class UserRegisterService implements UserRegister {

    private final KeycloakRegister keycloakRegister;
    private final UserCreate userCreate;

    @Override
    public User register(@Valid UserRegisterRequest request) {
        KeycloakRegisterRequest keycloakRegisterRequest = KeycloakRegisterRequest.builder()
                .username(request.username())
                .password(request.password())
                .build();
        KeycloakUser keycloakUser = keycloakRegister.register(keycloakRegisterRequest);

        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .keycloakUser(keycloakUser)
                .nickname(request.nickname())
                .userPhone(request.userPhone())
                .currentAddress(request.currentAddress())
                .build();
        return userCreate.create(userCreateRequest);
    }
}
