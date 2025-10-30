package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record StoreId(String id) {
}
