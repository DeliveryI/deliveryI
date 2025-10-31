package com.sparta.deliveryi.store.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record StoreRegisterRequest(
        @NotNull UUID ownerId,
        @NotNull String category,
        @NotNull String name,
        @NotNull String description,
        @NotNull String address,
        @NotNull @Size(max = 20) String phone
) {
}
