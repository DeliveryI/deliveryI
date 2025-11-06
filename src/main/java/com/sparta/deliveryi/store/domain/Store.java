package com.sparta.deliveryi.store.domain;

import com.sparta.deliveryi.global.domain.AbstractEntity;
import com.sparta.deliveryi.global.domain.Rating;
import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.store.domain.event.StoreRemoveEvent;
import com.sparta.deliveryi.store.domain.event.StoreTransferEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Table(name = "p_store")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends AbstractEntity {

    @EmbeddedId
    private StoreId id;

    @Embedded
    private Owner owner;

    @Embedded
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
    private Rating rating;

    private AvailableAddress availableAddress;

    private String operationHours;

    private String closedDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_status", nullable = false)
    private StoreStatus status;

    public static Store registerRequest(StoreRegisterRequest registerRequest) {
        Store store = new Store();

        store.id = StoreId.generateId();
        store.owner = Owner.of(registerRequest.ownerId());
        store.category = Category.of(registerRequest.category());
        store.name = requireNonNull(registerRequest.name());
        store.description = requireNonNull(registerRequest.description());
        store.address = requireNonNull(registerRequest.address());
        store.phone = requireNonNull(registerRequest.phone());

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
        if (this.status != StoreStatus.READY) {
            throw new StoreException(StoreMessageCode.STATUS_IS_NOT_READY);
        }

        this.status = StoreStatus.OPEN;
    }

    public void close() {
        if (this.status != StoreStatus.OPEN) {
            throw new StoreException(StoreMessageCode.STATUS_IS_NOT_OPEN);
        }

        this.status = StoreStatus.READY;
    }

    public void updateInfo(StoreInfoUpdateRequest updateRequest) {
        state(this.status != StoreStatus.PENDING, "등록 대기 상태에서는 가게 정보를 수정할 수 없습니다.");

        this.name = requireNonNull(updateRequest.name());
        this.category = Category.of(requireNonNull(updateRequest.category()));
        this.description = requireNonNull(updateRequest.description());
        this.address = requireNonNull(updateRequest.address());
        this.phone = requireNonNull(updateRequest.phone());
        this.notice = updateRequest.notice();
        this.availableAddress = AvailableAddress.from(updateRequest.availableAddress());
        this.operationHours = updateRequest.operationHours();
        this.closedDays = updateRequest.closedDays();
    }

    public void transferOwnership(UUID ownerId) {
        state(this.status != StoreStatus.PENDING, "등록 대기 상태에서는 인수인계할 수 없습니다.");

        UUID oldOwnerId = owner.getId();

        owner = Owner.of(requireNonNull(ownerId));

        Events.trigger(new StoreTransferEvent(oldOwnerId, ownerId));
    }

    public void updateRating(Rating rating) {
        state(this.status != StoreStatus.PENDING, "등록 대기 상태에서는 평점을 갱신할 수 없습니다.");

        this.rating = requireNonNull(rating);
    }

    public void remove() {
        super.delete();
        this.status = StoreStatus.REMOVED;

        Events.trigger(new StoreRemoveEvent(owner.getId()));
    }
}
