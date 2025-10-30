package com.sparta.deliveryi.store.domain;

import java.util.UUID;

public record StoreRegisterRequest(
        UUID ownerId,
        String category,
        String name,
        String description,
        String address,
        String phone
) {
}
