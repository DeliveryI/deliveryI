package com.sparta.deliveryi.store.domain;

import java.util.List;

public record StoreInfoUpdateRequest(
        String name,
        String category,
        String description,
        String address,
        String phone,
        String notice,
        List<String> availableAddress,
        String operationHours,
        String closedDays
) {
}
