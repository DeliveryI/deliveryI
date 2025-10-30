package com.sparta.deliveryi.store.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StoreTest {

    @Test
    void registerRequest() {
        StoreRegisterRequest registerRequest = new StoreRegisterRequest(
                UUID.randomUUID(),
                "KOREAN",
                "홍길동",
                "한식 가게입니다.",
                "서울시 강남구 테스트로 12",
                "02-1234-1234"
                );

        Store store = Store.registerRequest(registerRequest);

        assertThat(store.getCategory()).isEqualTo(new Category("KOREAN"));
        assertThat(store.getName()).isEqualTo("홍길동");
        assertThat(store.getDescription()).isEqualTo("한식 가게입니다.");
        assertThat(store.getAddress()).isEqualTo("서울시 강남구 테스트로 12");
        assertThat(store.getPhone()).isEqualTo("02-1234-1234");
        assertThat(store.getRating().score()).isEqualTo("0.0");
        assertThat(store.getStatus()).isEqualTo(StoreStatus.PENDING);
    }
}