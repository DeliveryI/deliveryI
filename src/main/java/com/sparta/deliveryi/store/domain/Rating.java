package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Rating {
    @Column(name = "rating", nullable = false)
    private final Float score;

    public Rating() {
        this.score = 0.0f;
    }

    public Rating(Float score) {
        this.score = score;
    }

    public String score() {
        return String.format("%.1f", this.score);
    }
}
