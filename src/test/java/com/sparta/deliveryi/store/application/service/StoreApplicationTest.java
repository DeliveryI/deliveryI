package com.sparta.deliveryi.store.application.service;

import com.sparta.deliveryi.DeliveryITestConfiguration;
import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Owner;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreStatus;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@Import(DeliveryITestConfiguration.class)
class StoreApplicationTest {
    private static final UUID MANAGER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Autowired
    private StoreApplication storeApplication;

    @Autowired
    private StoreRegister storeRegister;

    @Autowired
    private StoreFinder storeFinder;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private UserRolePolicy rolePolicy;

    @Test
    void updateInfo() {
        Store store = registerStore();
        StoreInfoUpdateRequest updateRequest = StoreFixture.createStoreUpdateRequest();
        when(rolePolicy.isAdmin(store.getOwner().getId())).thenReturn(false);

        store = storeApplication.updateInfo(store.getId().toUuid(), updateRequest, store.getOwner().getId());

        assertThat(store.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void updateInfoIfManager() {
        Store store = registerStore();
        StoreInfoUpdateRequest updateRequest = StoreFixture.createStoreUpdateRequest();
        when(rolePolicy.isAdmin(MANAGER_UUID)).thenReturn(true);

        store = storeApplication.updateInfo(store.getId().toUuid(), updateRequest, MANAGER_UUID);

        assertThat(store.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void remove() {
        Store store = registerStore();
        when(rolePolicy.isAdmin(store.getOwner().getId())).thenReturn(false);

        storeApplication.remove(store.getId().toUuid(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REMOVED);
    }

    @Test
    void removeIfManager() {
        Store store = registerStore();
        when(rolePolicy.isAdmin(MANAGER_UUID)).thenReturn(true);

        storeApplication.remove(store.getId().toUuid(), MANAGER_UUID);
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.REMOVED);
    }

    @Test
    void open() {
        Store store = registerStore();
        when(rolePolicy.isAdmin(store.getOwner().getId())).thenReturn(false);

        storeApplication.open(store.getId().toUuid(), store.getOwner().getId());
        entityManager.flush();
        entityManager.clear();
        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void openIfManager() {
        Store store = registerStore();
        when(rolePolicy.isAdmin(MANAGER_UUID)).thenReturn(true);

        storeApplication.open(store.getId().toUuid(), MANAGER_UUID);
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void close() {
        Store store = registerStore();
        storeApplication.open(store.getId().toUuid(), store.getOwner().getId());
        when(rolePolicy.isAdmin(store.getOwner().getId())).thenReturn(false);

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
        when(rolePolicy.isAdmin(MANAGER_UUID)).thenReturn(true);

        storeApplication.close(store.getId().toUuid(), MANAGER_UUID);
        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getStatus()).isEqualTo(StoreStatus.READY);
    }

    @Test
    void transfer() {
        Store store = registerStore();
        UUID newOwnerId = UUID.randomUUID();
        when(rolePolicy.isAdmin(store.getOwner().getId())).thenReturn(false);

        storeApplication.transfer(store.getId().toUuid(), newOwnerId, store.getOwner().getId());

        entityManager.flush();
        entityManager.clear();

        store = storeFinder.find(store.getId());

        assertThat(store.getOwner()).isEqualTo(Owner.of(newOwnerId));
    }

    private Store registerStore() {
        Store store = storeRegister.register(StoreFixture.createStoreRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        storeRegister.acceptRegisterRequest(store.getId().toUuid(), UUID.randomUUID());

        return store;
    }

}