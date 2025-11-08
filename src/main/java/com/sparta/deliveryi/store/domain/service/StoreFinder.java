package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Owner;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreSearchCondition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StoreFinder {
    Store find(StoreId storeId);

    List<Store> findByOwner(Owner owner);

    Page<Store> search(StoreSearchCondition searchCondition);
}
