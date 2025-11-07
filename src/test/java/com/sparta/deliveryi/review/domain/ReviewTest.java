package com.sparta.deliveryi.review.domain;

import com.sparta.deliveryi.global.domain.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.sparta.deliveryi.review.ReviewFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    Review review;

    @BeforeEach
    void setUp() {
        review = createReview(createReviewRegisterRequest());
    }

    @Test
    void register() {
        ReviewRegisterRequest request = createReviewRegisterRequest();

        review = Review.register(request);

        assertThat(review.getId()).isNotNull();
        assertThat(review.getRating()).isEqualTo(Rating.of(request.rating()));
        assertThat(review.getContent()).isEqualTo(request.content());
    }

    @Test
    void update() {
        ReviewUpdateRequest request = createReviewUpdateRequest();

        review.update(request);

        assertThat(review.getRating()).isEqualTo(Rating.of(request.rating()));
        assertThat(review.getContent()).isEqualTo(request.content());
    }

    @Test
    void remove() {
        review.remove();

        assertThat(review.getDeletedAt()).isNotNull();
    }
}