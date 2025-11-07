package com.sparta.deliveryi.review.application;

import com.sparta.deliveryi.global.domain.Rating;
import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.review.application.event.RatineCalculatedEvent;
import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewId;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import com.sparta.deliveryi.review.domain.service.*;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewApplicationService implements ReviewApplication {

    private final ReviewManager reviewManager;

    private final ReviewFinder reviewFinder;

    private final UserRolePolicy userRolePolicy;

    private final ReviewRegister reviewRegister;

    private static boolean isNotReviewer(UUID requestId, Review review) {
        return !review.getReviewer().getId().equals(requestId);
    }

    @Override
    public Review register(ReviewRegisterRequest registerRequest, UUID requestId) {
        Review review = reviewRegister.register(registerRequest);

        calculateAverageRating(review.getStoreId());

        return review;
    }

    @Override
    public Review update(Long reviewId, ReviewUpdateRequest updateRequest, UUID requestId) {
        ReviewId id = ReviewId.of(reviewId);

        validateUpdatePermission(id, requestId);

        Review review = reviewManager.update(id, updateRequest);
        calculateAverageRating(review.getStoreId());

        return review;
    }

    @Override
    public Review remove(Long reviewId, UUID requestId) {
        ReviewId id = ReviewId.of(reviewId);

        validateRemovePermission(id, requestId);

        Review review = reviewManager.remove(id);
        calculateAverageRating(review.getStoreId());

        return review;
    }

    private void calculateAverageRating(UUID storeId) {
        List<Review> reviews = reviewFinder.findAllByStoreId(storeId);

        List<Rating> ratings = reviews.stream()
                .map(Review::getRating)
                .toList();

        Rating average = Rating.averageOf(ratings);

        Events.trigger(new RatineCalculatedEvent(storeId, average.getScore()));
    }

    private void validateRemovePermission(ReviewId id, UUID requestId) {
        if (isAdmin(requestId)) {
            return;
        }

        validateUpdatePermission(id, requestId);
    }

    private void validateUpdatePermission(ReviewId id, UUID requestId) {
        Review review = reviewFinder.find(id);

        if (isNotReviewer(requestId, review)) {
            throw new ReviewException(ReviewMessageCode.UPDATE_PERMISSION_DENIED);
        }
    }

    private boolean isAdmin(UUID requestId) {
        return userRolePolicy.isAdmin(requestId);
    }
}
