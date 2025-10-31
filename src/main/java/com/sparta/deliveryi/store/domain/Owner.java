package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner {

    @Column(name = "user_id", nullable = false)
    private UUID id;

    private Owner(UUID id) {
        this.id = id;
    }

    public static Owner of(UUID id) {
        return new Owner(id);
    }
}
