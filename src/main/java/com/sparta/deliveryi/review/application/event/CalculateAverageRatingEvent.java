package com.sparta.deliveryi.review.application.event;

import java.util.UUID;

public record CalculateAverageRatingEvent(UUID storeId, float rating) {
}
