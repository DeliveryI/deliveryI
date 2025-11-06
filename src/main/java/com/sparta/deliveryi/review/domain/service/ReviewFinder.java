package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewId;

public interface ReviewFinder {
    Review find(ReviewId reviewId);
}
