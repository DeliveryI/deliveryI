package com.sparta.deliveryi.store.application.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;

import java.util.UUID;

public interface StoreApplication {
    Store updateInfo(UUID storeId, StoreInfoUpdateRequest updateRequest, UUID requestId);
    Store remove(UUID storeId, UUID requestId);
    void open(UUID storeId, UUID requestId);
    void close(UUID storeId, UUID requestId);
}
