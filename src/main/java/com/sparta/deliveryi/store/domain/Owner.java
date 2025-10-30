package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record Owner(UUID id) {
}
