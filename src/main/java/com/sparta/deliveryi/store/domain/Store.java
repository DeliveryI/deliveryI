package com.sparta.deliveryi.store.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends AbstractEntity {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_id")
    private StoreId id;

    @Embedded
    @Column(name = "user_id", nullable = false)
    private Owner owner;

    @Embedded
    @Column(name = "store_category", nullable = false)
    private Category category;

    @Column(name = "store_name", nullable = false)
    private String name;

    @Column(name = "store_description", nullable = false)
    private String description;

    @Column(name = "store_address", nullable = false)
    private String address;

    @Column(name = "store_phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "store_notice")
    private String notice;

    @Embedded
    @Column(nullable = false)
    private Rating rating;

    private String availableAddress;

    private String operationHours;

    private String closedDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_status", nullable = false)
    private StoreStatus status;

    public static Store registerRequest(StoreRegisterRequest registerRequest) {
        Store store = new Store();

        store.owner = new Owner(registerRequest.ownerId());
        store.category = new Category(registerRequest.category());
        store.name = registerRequest.name();
        store.description = registerRequest.description();
        store.address = registerRequest.address();
        store.phone = registerRequest.phone();

        store.rating = new Rating();
        store.status = StoreStatus.PENDING;

        return store;
    }
}
