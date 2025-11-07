package com.sparta.deliveryi.store.event;

import java.util.UUID;

public record StoreTransferEvent(UUID ownerId, UUID newOwnerId) {
}
