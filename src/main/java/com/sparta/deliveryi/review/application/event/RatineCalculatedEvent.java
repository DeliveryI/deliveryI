package com.sparta.deliveryi.review.application.event;

import java.util.UUID;

public record RatineCalculatedEvent(UUID storeId, float rating) {
}
