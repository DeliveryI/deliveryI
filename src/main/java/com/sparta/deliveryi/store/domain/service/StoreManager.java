package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.StoreId;

import java.util.UUID;

public interface StoreManager {
    void open(StoreId storeId, UUID requestId);
    void forcedOpen(StoreId storeId);
    void close(StoreId storeId, UUID requestId);
    void forcedClose(StoreId storeId);
    void remove(StoreId storeId, UUID requestId);
    void forcedRemove(StoreId storeId);
}
