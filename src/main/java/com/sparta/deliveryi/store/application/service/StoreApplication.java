package com.sparta.deliveryi.store.application.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StoreApplication {
    Page<Store> search(UUID ownerId, String keyword, UUID requestId, Pageable pageable);

    Store updateInfo(UUID storeId, StoreInfoUpdateRequest updateRequest, UUID requestId);

    Store remove(UUID storeId, UUID requestId);

    void open(UUID storeId, UUID requestId);

    void close(UUID storeId, UUID requestId);

    void transfer(UUID storeId, UUID newOwnerId, UUID requestId);
}
