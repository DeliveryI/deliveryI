package com.sparta.deliveryi.store.application.service;

import com.sparta.deliveryi.DeliveryITestConfiguration;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.StoreStatus;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
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
record StoreApplicationTest(
        StoreApplication storeApplication,
        StoreRegister storeRegister,
        StoreFinder storeFinder,
        EntityManager entityManager
) {
    @Test
    void register() {
        StoreRegisterRequest request = StoreFixture.createStoreRegisterRequest();

        Store store = storeApplication.register(request, UUID.randomUUID());

        assertThat(store.getId()).isNotNull();
    }

    @Test
    void updateInfo() {
        Store store = registerStore();

        StoreInfoUpdateRequest updateRequest = StoreFixture.createStoreUpdateRequest();

        store = storeApplication.updateInfo(store.getId().toUuid(), updateRequest, store.getOwner().getId());

        assertThat(store.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void updateInfoIfManager() {
        Store store = registerStore();

        StoreInfoUpdateRequest updateRequest = StoreFixture.createStoreUpdateRequest();

        store = storeApplication.updateInfo(store.getId().toUuid(), updateRequest, UUID.fromString("00000000-0000-0000-0000-000000000000"));

        assertThat(store.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void remove() {
        Store store = registerStore();

        storeApplication.remove(store.getId().toUuid(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REMOVED);
    }

    @Test
    void removeIfManager() {
        Store store = registerStore();

        storeApplication.remove(store.getId().toUuid(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REMOVED);
    }

    @Test
    void open() {
        Store store = registerStore();

        storeApplication.open(store.getId().toUuid(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();
        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void openIfManager() {
        Store store = registerStore();

        storeApplication.open(store.getId().toUuid(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void close() {
        Store store = registerStore();
        storeApplication.open(store.getId().toUuid(), store.getOwner().getId());

        storeApplication.close(store.getId().toUuid(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.READY);
    }

    @Test
    void closeIfManager() {
        Store store = registerStore();
        storeApplication.open(store.getId().toUuid(), store.getOwner().getId());

        storeApplication.close(store.getId().toUuid(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.READY);
    }

    private Store registerStore() {
        Store store = storeRegister.register(StoreFixture.createStoreRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        storeRegister.acceptRegisterRequest(store.getId().toUuid());

        return store;
    }

}