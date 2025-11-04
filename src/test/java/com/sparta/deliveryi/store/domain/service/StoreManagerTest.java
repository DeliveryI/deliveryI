package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
record StoreManagerTest(StoreRegister storeRegister, StoreManager storeManager, StoreFinder storeFinder, EntityManager entityManager) {

    @Test
    void updateInfo() {
        Store store = registerStore();

        StoreInfoUpdateRequest updateRequest = StoreFixture.createStoreUpdateRequest();

        store = storeManager.updateInfo(store.getId(), updateRequest, store.getOwner().getId());

        assertThat(store.getName()).isEqualTo(updateRequest.name());
        assertThat(store.getCategory().getName()).isEqualTo(updateRequest.category());
        assertThat(store.getDescription()).isEqualTo(updateRequest.description());
        assertThat(store.getAddress()).isEqualTo(updateRequest.address());
        assertThat(store.getPhone()).isEqualTo(updateRequest.phone());
        assertThat(store.getNotice()).isEqualTo(updateRequest.notice());
        assertThat(store.getAvailableAddress()).containsExactlyInAnyOrderElementsOf(updateRequest.availableAddress());
        assertThat(store.getOperationHours()).isEqualTo(updateRequest.operationHours());
        assertThat(store.getClosedDays()).isEqualTo(updateRequest.closedDays());
    }

    @Test
    void updateInfoIfNotOwner() {
        Store store = registerStore();

        StoreInfoUpdateRequest updateRequest = StoreFixture.createStoreUpdateRequest();

        assertThatThrownBy(() -> storeManager.updateInfo(store.getId(), updateRequest, randomUUID()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void forcedUpdateInfo() {
        Store store = registerStore();

        StoreInfoUpdateRequest updateRequest = StoreFixture.createStoreUpdateRequest();

        store = storeManager.forcedUpdateInfo(store.getId(), updateRequest);

        assertThat(store.getName()).isEqualTo(updateRequest.name());
        assertThat(store.getCategory().getName()).isEqualTo(updateRequest.category());
        assertThat(store.getDescription()).isEqualTo(updateRequest.description());
        assertThat(store.getAddress()).isEqualTo(updateRequest.address());
        assertThat(store.getPhone()).isEqualTo(updateRequest.phone());
        assertThat(store.getNotice()).isEqualTo(updateRequest.notice());
        assertThat(store.getAvailableAddress()).containsExactlyInAnyOrderElementsOf(updateRequest.availableAddress());
        assertThat(store.getOperationHours()).isEqualTo(updateRequest.operationHours());
        assertThat(store.getClosedDays()).isEqualTo(updateRequest.closedDays());
    }

    @Test
    void remove() {
        Store store = registerStore();

        storeManager.remove(store.getId(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REMOVED);
    }

    @Test
    void removeIfNotOwner() {
        Store store = registerStore();

        assertThatThrownBy(() -> storeManager.remove(store.getId(), randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void forcedRemove() {
        Store store = registerStore();

        storeManager.forcedRemove(store.getId());
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REMOVED);
    }

    @Test
    void open() {
        Store store = registerStore();

        storeManager.open(store.getId(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();
        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void openIfNotOwner() {
        Store store = registerStore();

        assertThatThrownBy(() -> storeManager.open(store.getId(), randomUUID()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void forcedOpen() {
        Store store = registerStore();

        storeManager.forcedOpen(store.getId());
        entityManager.flush();
        entityManager.clear();
        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void close() {
        Store store = registerStore();
        storeManager.forcedOpen(store.getId());

        storeManager.close(store.getId(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.READY);
    }

    @Test
    void closeIfNotOwner() {
        Store store = registerStore();
        storeManager.forcedOpen(store.getId());

        assertThatThrownBy(() -> storeManager.close(store.getId(), randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void forcedClose() {
        Store store = registerStore();
        storeManager.forcedOpen(store.getId());

        storeManager.forcedClose(store.getId());
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