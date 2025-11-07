package com.sparta.deliveryi.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

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

    public static Rating of(int score) {
        return of((float) score);
    }

    public static Rating of(Float score) {
        if (score == null || score < 0.0f || score > 5.0f) {
            throw new IllegalArgumentException("평점은 0.0 이상 5.0 이하이어야 합니다.");
        }

        return new Rating(score);
    }

    public static Rating averageOf(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return Rating.of(0.0f);
        }

        double sum = ratings.stream()
                .mapToDouble(Rating::getScore)
                .sum();

        float average = (float) (sum / ratings.size());
        return Rating.of(roundToOneDecimal(average));
    }

    private static float roundToOneDecimal(float value) {
        return Math.round(value * 10.0f) / 10.0f;
    }

    public String score() {
        return String.format("%.1f", this.score);
    }
}
