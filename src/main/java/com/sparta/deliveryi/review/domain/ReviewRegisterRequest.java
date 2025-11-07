package com.sparta.deliveryi.review.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ReviewRegisterRequest(
        @NotNull UUID storeId,
        @NotNull UUID orderId,
        @NotNull UUID reviewerId,
        @NotNull @Max(5) @Min(1) int rating,
        @NotNull @Size(max = 255) String content
) {
}
