package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewId;
import com.sparta.deliveryi.review.domain.ReviewRepository;
import com.sparta.deliveryi.review.domain.Reviewer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryService implements ReviewFinder {

    private final ReviewRepository reviewRepository;

    @Override
    public Review find(ReviewId reviewId) {
        return reviewRepository.findById(reviewId.value())
                .orElseThrow(() -> new IllegalArgumentException("리뷰 정보를 찾을 수 없습니다. id: " + reviewId));
    }

    @Override
    public List<Review> findAllByStoreId(UUID storeId) {
        return reviewRepository.findAllByStoreIdAndDeletedAtIsNotNull(storeId);
    }

    @Override
    public List<Review> findAllByReviewer(Reviewer reviewer) {
        return reviewRepository.findAllByReviewer(reviewer);
    }

}
