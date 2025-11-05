package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface StoreRegister {
    Store register(@Valid StoreRegisterRequest registerRequest);

    void acceptRegisterRequest(@NotNull UUID storeId, @NotNull UUID requestId);

    void rejectRegisterRequest(@NotNull UUID storeId);
}
