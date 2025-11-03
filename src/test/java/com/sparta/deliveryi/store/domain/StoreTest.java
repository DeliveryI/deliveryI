package com.sparta.deliveryi.store.domain;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.sparta.deliveryi.store.StoreFixture.createStore;
import static com.sparta.deliveryi.store.StoreFixture.createStoreInfoUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreTest {

    Store store;

    @BeforeEach
    void setUp() {
        store = createStore();
        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    @Test
    void registerRequest() {
        assertThat(store.getCategory()).isEqualTo(Category.of("KOREAN"));
        assertThat(store.getName()).isEqualTo("홍길동");
        assertThat(store.getDescription()).isEqualTo("한식 가게입니다.");
        assertThat(store.getAddress()).isEqualTo("서울시 강남구 테스트로 12");
        assertThat(store.getPhone()).isEqualTo("02-1234-1234");
        assertThat(store.getRating().score()).isEqualTo("0.0");
        assertThat(store.getStatus()).isEqualTo(StoreStatus.PENDING);
    }

    @Test
    void rejectRequest() {
        store.rejectRegisterRequest();

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REJECTED);
    }

    @Test
    void rejectRequestIfNotPending() {
        store.acceptRegisterRequest();

        assertThatThrownBy(() -> store.rejectRegisterRequest())
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    void acceptRequest() {
        store.acceptRegisterRequest();

        assertThat(store.getStatus()).isEqualTo(StoreStatus.READY);
    }

    @Test
    void acceptRequestIfNotPending() {
        store.rejectRegisterRequest();

        assertThatThrownBy(() -> store.acceptRegisterRequest())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void open() {
        store.acceptRegisterRequest();

        store.open();

        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void openIfNotReady() {
        store.acceptRegisterRequest();
        store.open();

        assertThatThrownBy(() -> store.open())
        .isInstanceOf(StoreException.class);
    }

    @Test
    void close() {
        store.acceptRegisterRequest();
        store.open();

        store.close();

        assertThat(store.getStatus()).isEqualTo(StoreStatus.READY);
    }

    @Test
    void closeIfNotOpen() {
        store.acceptRegisterRequest();

        assertThatThrownBy(() -> store.close())
                .isInstanceOf(StoreException.class);
    }

    @Test
    void updateInfo() {
        StoreInfoUpdateRequest updateRequest = createStoreInfoUpdateRequest();
        store.acceptRegisterRequest();

        store.updateInfo(updateRequest);

        assertThat(store.getCategory()).isEqualTo(Category.of("SNACK_FOOD"));
        assertThat(store.getName()).isEqualTo("홍길동 분식");
        assertThat(store.getDescription()).isEqualTo("분식 가게입니다.");
        assertThat(store.getAddress()).isEqualTo("서울시 강남구 테스트로 13");
        assertThat(store.getPhone()).isEqualTo("02-1234-5678");
        assertThat(store.getAvailableAddress()).contains("강남구", "관악구", "강동구");
        assertThat(store.getOperationHours()).isEqualTo("06:00 ~ 22:00");
        assertThat(store.getClosedDays()).isEqualTo("매주 일요일");
    }

    @Test
    void updateInfoIfPendingStatus() {
        StoreInfoUpdateRequest updateRequest = createStoreInfoUpdateRequest();

        assertThatThrownBy(() -> store.updateInfo(updateRequest))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void updateInfoIfNullValue() {
        StoreInfoUpdateRequest updateRequest = new StoreInfoUpdateRequest(
                null,
                "SNACK_FOOD",
                "분식 가게입니다.",
                "서울시 강남구 테스트로 13",
                "02-1234-5678",
                "매주 일요일 휴무입니다.",
                List.of("강남구", "관악구", "강동구"),
                "06:00 ~ 22:00",
                "매주 일요일"
        );
        store.acceptRegisterRequest();

        assertThatThrownBy(() -> store.updateInfo(updateRequest))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void transferOwnership() {
        store.acceptRegisterRequest();
        UUID ownerId = UUID.randomUUID();

        store.transferOwnership(ownerId);

        assertThat(store.getOwner()).isEqualTo(Owner.of(ownerId));
    }

    @Test
    void transferOwnerShipIfPendingStatus() {
        assertThatThrownBy(() -> store.transferOwnership(null))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void transferOwnerShipIfNullId() {
        store.acceptRegisterRequest();

        assertThatThrownBy(() -> store.transferOwnership(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateRating() {
        store.acceptRegisterRequest();

        store.updateRating(Rating.of(5.0f));

        assertThat(store.getRating().score()).isEqualTo("5.0");
    }

    @Test
    void updateRatingIfPendingStatus() {
        assertThatThrownBy(() -> store.updateRating(Rating.of(5.0f)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void updateRatingIfNullRating() {
        store.acceptRegisterRequest();

        assertThatThrownBy(() -> store.updateRating(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void remove() {
        store.remove();

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REMOVED);
        assertThat(store.getDeletedAt()).isNotNull();
        assertThat(store.getDeletedBy()).isNotNull();
    }
}