package com.sparta.deliveryi.review.application.event;

import java.util.UUID;

public record RatingCalculatedEvent(UUID storeId, float rating) {
}
