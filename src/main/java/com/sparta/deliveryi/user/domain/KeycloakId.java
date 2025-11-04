package com.sparta.deliveryi.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeycloakId {

    @Column(name = "keycloak_id", nullable = false)
    private UUID id;

    protected KeycloakId(UUID id) {
        this.id = id;
    }

    public static KeycloakId of(UUID id) {
        return new KeycloakId(id);
    }

    public static KeycloakId of(String id) {
        return new KeycloakId(UUID.fromString(id));
    }
}
