package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.DeliveryITestConfiguration;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(DeliveryITestConfiguration.class)
record StoreRegisterTest(StoreRegister storeRegister, StoreFinder storeFinder, EntityManager entityManager) {

    @Test
    void register() {
        Store store = storeRegister.register(StoreFixture.createStoreRegisterRequest());

        assertThat(store.getId()).isNotNull();
        assertThat(store.getStatus()).isEqualTo(StoreStatus.PENDING);
        assertThat(store.getRating().score()).isEqualTo("0.0");
    }

    @Test
    void acceptRegisterRequest() {
        Store store = registerStore();

        storeRegister.acceptRegisterRequest(store.getId().toUuid(), UUID.randomUUID());
        entityManager.flush();
        entityManager.clear();
        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.READY);
    }

    @Test
    void rejectRegisterRequest() {
        Store store = registerStore();

        storeRegister.rejectRegisterRequest(store.getId().toUuid());
        entityManager.flush();
        entityManager.clear();
        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REJECTED);
    }

    private Store registerStore() {
        Store store = storeRegister.register(StoreFixture.createStoreRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return store;
    }
}