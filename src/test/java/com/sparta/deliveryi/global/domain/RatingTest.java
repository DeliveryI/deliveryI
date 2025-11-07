package com.sparta.deliveryi.global.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RatingTest {
    @Test
    @DisplayName("여러 Rating의 평균을 계산하면 소수 첫째 자리에서 반올림된 Rating이 반환된다")
    void averageShouldReturnRoundedAverageRating() {
        List<Rating> ratings = List.of(
                Rating.of(4.15f),
                Rating.of(3.85f),
                Rating.of(4.27f)
        );

        Rating average = Rating.averageOf(ratings);

        assertThat(average.getScore()).isEqualTo(4.1f);
        assertThat(average.score()).isEqualTo("4.1");
    }

    @Test
    @DisplayName("평점이 비어있는 경우 0.0으로 초기화된 Rating을 반환한다")
    void averageShouldReturnZeroWhenEmptyList() {
        List<Rating> ratings = List.of();

        Rating average = Rating.averageOf(ratings);

        assertThat(average.getScore()).isEqualTo(0.0f);
        assertThat(average.score()).isEqualTo("0.0");
    }

    @Test
    @DisplayName("평점이 null이거나 범위를 벗어나면 예외가 발생한다")
    void shouldThrowExceptionWhenInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> Rating.of(-1.0f));
        assertThrows(IllegalArgumentException.class, () -> Rating.of(6.0f));
        assertThrows(IllegalArgumentException.class, () -> Rating.of(null));
    }

    @Test
    @DisplayName("평균 평점이 정확히 소수 첫째 자리까지 반올림되어야 한다")
    void averageShouldRoundToOneDecimalPlace() {
        List<Rating> ratings = List.of(
                Rating.of(4.14f),
                Rating.of(4.15f)
        );

        Rating average = Rating.averageOf(ratings);

        assertThat(average.getScore()).isEqualTo(4.1f);
    }
}