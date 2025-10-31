package com.sparta.deliveryi.store;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;

import java.util.List;
import java.util.UUID;

public class StoreFixture {

    public static StoreInfoUpdateRequest createStoreInfoUpdateRequest() {

        return new StoreInfoUpdateRequest(
                "홍길동 분식",
                "SNACK_FOOD",
                "분식 가게입니다.",
                "서울시 강남구 테스트로 13",
                "02-1234-5678",
                "매주 일요일 휴무입니다.",
                List.of("강남구", "관악구", "강동구"),
                "06:00 ~ 22:00",
                "매주 일요일"
        );
    }

    public static Store createStore() {
        StoreRegisterRequest registerRequest = createStoreRegisterRequest();

        return Store.registerRequest(registerRequest);
    }

    public static StoreRegisterRequest createStoreRegisterRequest() {
        return new StoreRegisterRequest(
                UUID.randomUUID(),
                "KOREAN",
                "홍길동",
                "한식 가게입니다.",
                "서울시 강남구 테스트로 12",
                "02-1234-1234"
        );
    }
}
