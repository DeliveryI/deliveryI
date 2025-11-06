package com.sparta.deliveryi.user.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.user.domain.dto.UserInfoUpdateRequest;
import com.sparta.deliveryi.user.domain.dto.UserCreateRequest;
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
    private UserPhone userPhone;

    @Column(name = "current_address")
    private String currentAddress;

    public static User create(UserCreateRequest request) {
        User user = new User();

        Objects.requireNonNull(request.authUser());
        user.keycloakId = KeycloakId.of(request.authUser().id());
        user.username = request.authUser().username();
        user.role = request.authUser().role();

        user.id = Objects.requireNonNull(UserId.of(request.authUser().id()));
        user.nickname = Objects.requireNonNull(request.nickname());
        user.userPhone = Objects.requireNonNull(UserPhone.of(request.userPhone()));
        user.currentAddress = request.currentAddress();

        user.createBy(request.authUser().username());

        return user;
    }

    public void updateInfo(UserInfoUpdateRequest updateRequest) {
        this.nickname = Objects.requireNonNull(updateRequest.nickname());
        this.userPhone = Objects.requireNonNull(UserPhone .of(updateRequest.userPhone()));
        this.currentAddress = updateRequest.currentAddress();
    }

    public void updateRole(UserRole role, String updatedBy) {
        this.role = Objects.requireNonNull(role);

        Objects.requireNonNull(updatedBy);
        this.updateBy(updatedBy);
    }
}
