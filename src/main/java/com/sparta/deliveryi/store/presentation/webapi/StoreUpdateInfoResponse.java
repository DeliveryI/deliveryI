package com.sparta.deliveryi.store.presentation.webapi;

import com.sparta.deliveryi.store.domain.Store;

public record StoreUpdateInfoResponse(String storeId) {
    public static StoreUpdateInfoResponse from(Store store) {
        return new StoreUpdateInfoResponse(store.getId().toString());
    }
}
