package com.sparta.deliveryi.store.event;

import java.util.UUID;

public record StoreRegisterAcceptEvent(UUID userId, UUID updatedBy) {
}
