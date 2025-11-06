package com.sparta.deliveryi.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class Rating {
    @Column(name = "rating", nullable = false)
    private final Float score;

    public Rating() {
        this.score = 0.0f;
    }

    private Rating(Float score) {
        this.score = score;
    }

    public static Rating of(Float score) {
        return new Rating(score);
    }

    public String score() {
        return String.format("%.1f", this.score);
    }
}
