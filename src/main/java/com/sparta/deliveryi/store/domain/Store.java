package com.sparta.deliveryi.store.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.store.infrastructure.AvailableAddressConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static org.springframework.util.Assert.*;

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

    @Convert(converter = AvailableAddressConverter.class)
    private List<String> availableAddress;

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

    public void rejectRegisterRequest() {
        state(this.status == StoreStatus.PENDING, "등록 대기 상태가 아닙니다.");
        this.status = StoreStatus.REJECTED;
    }

    public void acceptRegisterRequest() {
        state(this.status == StoreStatus.PENDING, "등록 대기 상태가 아닙니다.");

        this.status = StoreStatus.READY;
    }

    public void open() {
        state(this.status == StoreStatus.READY, "준비중인 상태가 아닙니다.");

        this.status = StoreStatus.OPEN;
    }

    public void close() {
        state(this.status == StoreStatus.OPEN, "영업중인 상태가 아닙니다.");

        this.status = StoreStatus.READY;
    }
}
