package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.StoreFixture;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StoreRegisterTest {

    @Autowired
    private StoreRegister storeRegister;

    @Autowired
    private EntityManager entityManager;

    @Test
    void register() {
        Store store = storeRegister.register(StoreFixture.createStoreRegisterRequest());

        assertThat(store.getId()).isNotNull();
        assertThat(store.getStatus()).isEqualTo(StoreStatus.PENDING);
        assertThat(store.getRating().score()).isEqualTo("0.0");
    }
}