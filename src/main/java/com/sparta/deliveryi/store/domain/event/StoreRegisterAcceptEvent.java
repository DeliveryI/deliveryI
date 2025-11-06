package com.sparta.deliveryi.store.domain.event;

import java.util.UUID;

public record StoreRegisterAcceptEvent(UUID userId, UUID updatedBy) {
}
