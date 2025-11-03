package com.sparta.deliveryi.store.application.service;

import java.util.UUID;

public interface StoreApplication {
    void open(UUID storeId, UUID requestId);
    void close(UUID storeId, UUID requestId);
    void remove(UUID storeId, UUID requestId);
}
