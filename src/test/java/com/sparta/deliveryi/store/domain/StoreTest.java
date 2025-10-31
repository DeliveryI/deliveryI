package com.sparta.deliveryi.store.domain;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreTest {

    Store store;

    @BeforeEach
    void setUp() {
        store = createStoreRegisterRequest();
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

    private static Store createStoreRegisterRequest() {
        StoreRegisterRequest registerRequest = new StoreRegisterRequest(
                UUID.randomUUID(),
                "KOREAN",
                "홍길동",
                "한식 가게입니다.",
                "서울시 강남구 테스트로 12",
                "02-1234-1234"
        );

        return Store.registerRequest(registerRequest);
    }
}