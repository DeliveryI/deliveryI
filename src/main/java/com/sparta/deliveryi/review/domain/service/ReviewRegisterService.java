package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import com.sparta.deliveryi.review.domain.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class ReviewRegisterService implements ReviewRegister {

    private final ReviewRepository reviewRepository;

    @Override
    public Review register(ReviewRegisterRequest registerRequest) {
        Review review = Review.register(registerRequest);

        return reviewRepository.save(review);
    }
}
