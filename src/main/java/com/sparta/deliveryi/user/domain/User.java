package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import com.sparta.deliveryi.user.domain.dto.UserRegisterRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

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

        user.id = Objects.requireNonNull(UserId.generateId(), "UserId는 필수값입니다.");
        user.keycloakId = Objects.requireNonNull(KeycloakId.of(registerRequest.keycloakId()), "KeycloakId는 필수값입니다.");
        user.username = Objects.requireNonNull(registerRequest.username(), "username은 필수값입니다.");
        user.role = Objects.requireNonNull(registerRequest.role(), "role은 필수값입니다.");
        user.nickname = Objects.requireNonNull(registerRequest.nickname(), "nickname은 필수값입니다.");
        user.phoneNumber = Objects.requireNonNull(PhoneNumber.of(registerRequest.phoneNumber()), "phoneNumber는 필수값입니다.");
        user.currentAddress = registerRequest.currentAddress();

        return user;
    }

    public void updateInfo(UserInfoUpdateRequest updateRequest) {
        this.nickname = Objects.requireNonNull(updateRequest.nickname(), "nickname은 필수값입니다.");
        this.phoneNumber = Objects.requireNonNull(PhoneNumber.of(updateRequest.phoneNumber()), "phoneNumber는 필수값입니다.");
        this.currentAddress = updateRequest.currentAddress();
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }
}
