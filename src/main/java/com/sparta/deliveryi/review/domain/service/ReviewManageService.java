package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewId;
import com.sparta.deliveryi.review.domain.ReviewRepository;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class ReviewManageService implements ReviewManager {

    private final ReviewRepository reviewRepository;

    private final ReviewFinder reviewFinder;

    @Override
    public Review update(ReviewId reviewId, ReviewUpdateRequest updateRequest) {
        Review review = reviewFinder.find(reviewId);

        review.update(updateRequest);

        return reviewRepository.save(review);
    }

    @Override
    public Review remove(ReviewId reviewId) {
        Review review = reviewFinder.find(reviewId);

        review.remove();

        return reviewRepository.save(review);
    }
}
