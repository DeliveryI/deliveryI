package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.user.domain.dto.UserRegisterRequest;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "p_user")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractEntity {
    @EmbeddedId
    private UserId id;

    @Embedded
    private KeycloakId keycloakId;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "current_address")
    private String currentAddress;

    public static User register(UserRegisterRequest registerRequest) {
        User user = new User();

        user.id = UserId.of();
        user.keycloakId = KeycloakId.of(registerRequest.keycloakId());
        user.username = registerRequest.username();
        user.role = registerRequest.role();
        user.nickname = registerRequest.nickname();
        user.phoneNumber = PhoneNumber.of(registerRequest.phoneNumber());
        user.currentAddress = registerRequest.currentAddress();

        return user;
    }

    public void updateInfo(UserInfoUpdateRequest updateRequest) {
        this.nickname = updateRequest.nickname();
        this.phoneNumber = PhoneNumber.of(updateRequest.phoneNumber());
        this.currentAddress = updateRequest.currentAddress();
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }
}
