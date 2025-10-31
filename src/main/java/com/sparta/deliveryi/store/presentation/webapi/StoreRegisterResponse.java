package com.sparta.deliveryi.store.presentation.webapi;

import com.sparta.deliveryi.store.domain.Store;

public record StoreRegisterResponse(String storeId) {
    public static StoreRegisterResponse from(Store store) {
        return new StoreRegisterResponse(store.getId().toString());
    }
}
