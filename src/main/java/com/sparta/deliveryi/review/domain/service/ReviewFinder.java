package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewId;
import com.sparta.deliveryi.review.domain.Reviewer;

import java.util.List;
import java.util.UUID;

public interface ReviewFinder {
    Review find(ReviewId reviewId);

    List<Review> findAllByStoreId(UUID storeId);

    List<Review> findAllByReviewer(Reviewer reviewer);
}
