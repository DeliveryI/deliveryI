package com.sparta.deliveryi.review.domain.service;

import com.sparta.deliveryi.review.domain.Review;
import com.sparta.deliveryi.review.domain.ReviewRegisterRequest;
import jakarta.validation.Valid;

public interface ReviewRegister {
    Review register(@Valid ReviewRegisterRequest registerRequest);
}
