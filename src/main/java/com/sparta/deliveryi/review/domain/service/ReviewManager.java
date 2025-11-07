package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewId;
import com.sparta.deliveryi.review.domain.ReviewUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface ReviewManager {
    Review update(@NotNull ReviewId reviewId, @Valid ReviewUpdateRequest updateRequest);

    Review remove(@NotNull ReviewId reviewId);
}
