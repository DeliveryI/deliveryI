package com.sparta.deliveryi.store.presentation.webapi;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StoreTransferRequest(@NotNull UUID newOwnerId) {
}
