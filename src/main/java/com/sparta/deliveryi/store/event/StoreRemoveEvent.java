package com.sparta.deliveryi.store.event;

import java.util.UUID;

public record StoreRemoveEvent(UUID storeId, UUID userId) {
}
