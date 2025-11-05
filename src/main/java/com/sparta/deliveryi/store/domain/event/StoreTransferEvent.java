package com.sparta.deliveryi.store.domain.event;

import java.util.UUID;

public record StoreTransferEvent(UUID ownerId, UUID newOwnerId) {
}
