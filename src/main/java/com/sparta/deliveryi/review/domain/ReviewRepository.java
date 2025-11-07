package com.sparta.deliveryi.review.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends Repository<Review, Long> {
    Review save(Review review);

    Optional<Review> findById(Long reviewId);

    List<Review> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);

    List<Review> findAllByReviewer(Reviewer reviewer);

    @Query("SELECT r FROM Review r " +
            "WHERE (:storeId IS NULL OR r.storeId = :storeId) " +
            "AND (:reviewer IS NULL OR r.reviewer = :reviewer) " +
            "AND (:keyword IS NULL OR r.content LIKE %:keyword%) " +
            "AND (:includeDeleted = TRUE OR r.deletedAt IS NULL)")
    Page<Review> searchReviews(@Param("storeId") UUID storeId,
                               @Param("reviewer") Reviewer reviewer,
                               @Param("keyword") String keyword,
                               @Param("includeDeleted") boolean includeDeleted,
                               Pageable pageable);
}
