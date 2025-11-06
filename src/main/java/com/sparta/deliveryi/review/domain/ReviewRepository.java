package com.sparta.deliveryi.review.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ReviewRepository extends Repository<Review, Long> {
    Review save(Review review);

    Optional<Review> findById(Long reviewId);

}
