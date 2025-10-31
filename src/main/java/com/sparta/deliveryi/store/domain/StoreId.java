package com.sparta.deliveryi.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreId {
    @Column(name = "store_id")
    private UUID id;
}
