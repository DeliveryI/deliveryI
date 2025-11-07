package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return reviewRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);
    }

    @Override
    public List<Review> findAllByReviewer(Reviewer reviewer) {
        return reviewRepository.findAllByReviewer(reviewer);
    }

    @Override
    public Page<Review> search(ReviewSearchCondition searchCondition) {
        return reviewRepository.searchReviews(
                searchCondition.storeId(),
                searchCondition.reviewer(),
                searchCondition.keyword(),
                searchCondition.includeDeleted(),
                searchCondition.pageable()
        );
    }

}
