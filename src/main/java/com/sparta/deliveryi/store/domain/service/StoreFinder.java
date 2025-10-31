package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;

public interface StoreFinder {
    Store find(StoreId storeId);
}
