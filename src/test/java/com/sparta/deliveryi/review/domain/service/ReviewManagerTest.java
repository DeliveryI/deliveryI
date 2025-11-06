package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.global.domain.Rating;
import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.sparta.deliveryi.review.ReviewFixture.createReviewRegisterRequest;
import static com.sparta.deliveryi.review.ReviewFixture.createReviewUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
record ReviewManagerTest(
        ReviewRegister reviewRegister,
        ReviewManager reviewManager,
        ReviewFinder reviewFinder,
        EntityManager entityManager
) {

    @Test
    void update() {
        Review review = registerReview();
        ReviewUpdateRequest request = createReviewUpdateRequest();

        reviewManager.update(review.getId(), request);
        entityManager.flush();
        entityManager.clear();

        Review result = reviewFinder.find(review.getId());

        assertThat(result.getRating()).isEqualTo(Rating.of((float) request.rating()));
        assertThat(result.getContent()).isEqualTo(request.content());
    }

    @Test
    void remove() {
        Review review = registerReview();

        reviewManager.remove(review.getId());
        entityManager.flush();
        entityManager.clear();

        Review result = reviewFinder.find(review.getId());

        assertThat(result.getDeletedAt()).isNotNull();
    }

    private Review registerReview() {
        Review review = reviewRegister.register(createReviewRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        return review;
    }
}