package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Owner;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;

import java.util.List;

public interface StoreFinder {
    Store find(StoreId storeId);

    List<Store> findByOwner(Owner owner);
}
