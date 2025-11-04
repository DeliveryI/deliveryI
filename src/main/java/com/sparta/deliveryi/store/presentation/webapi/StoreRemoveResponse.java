package com.sparta.deliveryi.store.presentation.webapi;

import com.sparta.deliveryi.store.domain.Store;

public record StoreRemoveResponse(String storeId) {
    public static StoreRemoveResponse from(Store store) {
        return new StoreRemoveResponse(store.getId().toString());
    }
}
