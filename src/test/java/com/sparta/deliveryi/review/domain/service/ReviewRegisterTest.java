package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.sparta.deliveryi.review.ReviewFixture.createReviewRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
record ReviewRegisterTest(ReviewRegister reviewRegister, EntityManager entityManager) {

    @Test
    void register() {
        Review review = reviewRegister.register(createReviewRegisterRequest());

        assertThat(review.getId()).isNotNull();
    }

}