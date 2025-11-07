package com.sparta.deliveryi.review.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends Repository<Review, Long> {
    Review save(Review review);

    Optional<Review> findById(Long reviewId);

    List<Review> findAllByStoreIdAndDeletedAtIsNotNull(UUID storeId);

    List<Review> findAllByReviewer(Reviewer reviewer);

}
