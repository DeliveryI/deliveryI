package com.sparta.deliveryi.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orderer {

    @Column(name = "user_id", nullable = false)
    private UUID id;

    private Orderer(UUID id) {
        this.id = id;
    }

    public static Orderer of(UUID id) {
        return new Orderer(id);
    }
}
