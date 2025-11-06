package com.sparta.deliveryi.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewId {

    @Column(name = "store_id")
    private Long id;

    private ReviewId(Long id) {
        this.id = id;
    }

    public static ReviewId of(Long id) {
        return new ReviewId(id);
    }

    public Long value() {
        return id;
    }
}
