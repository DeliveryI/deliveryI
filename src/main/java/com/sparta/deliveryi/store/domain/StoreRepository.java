package com.sparta.deliveryi.store.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface StoreRepository extends Repository<Store, StoreId> {
    Store save(Store store);

    Optional<Store> findById(StoreId storeId);
}
