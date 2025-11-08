package com.sparta.deliveryi.store.presentation.webapi;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreStatus;

import java.util.List;

public record StoreSearchResponse(
        String name,
        String description,
        String address,
        String phone,
        String notice,
        String rating,
        List<String> availableAddress,
        String operationHours,
        String closedDays,
        StoreStatus status
) {
    public static StoreSearchResponse from(Store store) {
        return new StoreSearchResponse(
                store.getName(),
                store.getDescription(),
                store.getAddress(),
                store.getPhone(),
                store.getNotice(),
                store.getRating().score(),
                store.getAvailableAddress().getValues(),
                store.getOperationHours(),
                store.getClosedDays(),
                store.getStatus()
        );
    }
}
