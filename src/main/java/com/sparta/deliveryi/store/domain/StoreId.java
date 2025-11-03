package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreId {

    @Column(name = "store_id")
    private UUID id;

    private StoreId(UUID id) {
        this.id = id;
    }

    public static StoreId generateId() {
        return of(UUID.randomUUID());
    }

    public static StoreId of(UUID id) {
        return new StoreId(id);
    }

    public UUID toUuid() {
        return id;
    }

    public String toString(){
        return id.toString();
    }
}
