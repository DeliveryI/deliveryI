package com.sparta.deliveryi.review.application;

import com.sparta.deliveryi.global.domain.Rating;
import com.sparta.deliveryi.review.ReviewFixture;
import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import com.sparta.deliveryi.review.domain.service.ReviewException;
import com.sparta.deliveryi.review.domain.service.ReviewFinder;
import com.sparta.deliveryi.review.domain.service.ReviewRegister;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static com.sparta.deliveryi.review.ReviewFixture.createReviewRegisterRequest;
import static com.sparta.deliveryi.review.ReviewFixture.createReviewUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class ReviewApplicationTest {

    Review review;

    @Autowired
    private ReviewRegister reviewRegister;

    @Autowired
    private ReviewFinder reviewFinder;

    @Autowired
    private ReviewApplication reviewApplication;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private UserRolePolicy userRolePolicy;

    @BeforeEach
    void setUp() {
        review = createReview();
    }

    @Test
    void register() {
        ReviewRegisterRequest request = createReviewRegisterRequest();

        review = reviewApplication.register(request, UUID.randomUUID());

        assertThat(review.getId()).isNotNull();
    }

    @Test
    void update() {
        UUID requestId = review.getReviewer().getId();
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        ReviewUpdateRequest request = createReviewUpdateRequest();

        Review result = reviewApplication.update(review.getId().value(), request, requestId);

        assertThat(result.getContent()).isEqualTo(request.content());
        assertThat(result.getRating()).isEqualTo(Rating.of(request.rating()));
    }

    @Test
    void updateIfNotReviewer() {
        UUID requestId = UUID.randomUUID();
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);
        ReviewUpdateRequest request = createReviewUpdateRequest();

        assertThatThrownBy(() -> reviewApplication.update(review.getId().value(), request, requestId))
            .isInstanceOf(ReviewException.class);
    }

    @Test
    void updateIfAdmin() {
        UUID requestId = UUID.randomUUID();
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);
        ReviewUpdateRequest request = createReviewUpdateRequest();

        assertThatThrownBy(() -> reviewApplication.update(review.getId().value(), request, requestId))
                .isInstanceOf(ReviewException.class);
    }

    @Test
    void remove() {
        UUID requestId = review.getReviewer().getId();
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);

        Review result = reviewApplication.remove(review.getId().value(), requestId);

        assertThat(result.getDeletedAt()).isNotNull();
    }


    @Test
    void removeIfNotReviewer() {
        UUID requestId = review.getReviewer().getId();
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(false);

        Review result = reviewApplication.remove(review.getId().value(), requestId);

        assertThat(result.getDeletedAt()).isNotNull();
    }

    @Test
    void removeIfAdmin() {
        UUID requestId = UUID.randomUUID();
        when(userRolePolicy.isAdmin(eq(requestId))).thenReturn(true);

        Review result = reviewApplication.remove(review.getId().value(), requestId);

        assertThat(result.getDeletedAt()).isNotNull();
    }

    private Review createReview() {
        Review review = reviewRegister.register(ReviewFixture.createReviewRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        return review;
    }
}