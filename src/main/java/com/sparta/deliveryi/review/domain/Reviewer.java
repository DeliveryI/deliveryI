package com.sparta.deliveryi.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reviewer {

    @Column(name = "user_id", nullable = false)
    private UUID id;

    private Reviewer(UUID id) {
        this.id = id;
    }

    public static Reviewer of(UUID id) {
        return new Reviewer(id);
    }
}
