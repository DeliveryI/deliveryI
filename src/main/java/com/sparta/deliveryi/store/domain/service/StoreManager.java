package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface StoreManager {
    Store updateInfo(@NotNull StoreId storeId, @Valid StoreInfoUpdateRequest updateRequest, @NotNull UUID requestId);
    Store forcedUpdateInfo(@NotNull StoreId storeId, @Valid StoreInfoUpdateRequest updateRequest);
    Store remove(@NotNull StoreId storeId, @NotNull UUID requestId);
    Store forcedRemove(@NotNull StoreId storeId);
    void open(@NotNull StoreId storeId, @NotNull UUID requestId);
    void forcedOpen(@NotNull StoreId storeId);
    void close(@NotNull StoreId storeId, @NotNull UUID requestId);
    void forcedClose(@NotNull StoreId storeId);
}
