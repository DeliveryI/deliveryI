package com.sparta.deliveryi.review;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class ReviewFixture {

    public static Review createReview() {
        return createReview(createReviewRegisterRequest());
    }

    public static Review createReview(ReviewRegisterRequest request) {
        Review review = Review.register(request);
        ReflectionTestUtils.setField(review, "id", 1L);

        return review;
    }

    public static ReviewRegisterRequest createReviewRegisterRequest() {
        return new ReviewRegisterRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                4,
                "맛있어요."
        );
    }

    public static ReviewUpdateRequest createReviewUpdateRequest() {
        return new ReviewUpdateRequest(5, "정말 맛있어요.");
    }
}
