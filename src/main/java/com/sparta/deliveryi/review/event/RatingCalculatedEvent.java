package com.sparta.deliveryi.review.event;

import java.util.UUID;

public record RatingCalculatedEvent(UUID storeId, float rating) {
}
