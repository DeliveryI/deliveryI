package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class StoreManageService implements StoreManager {

    private final StoreRepository storeRepository;

    private final StoreFinder storeFinder;

    @Override
    public void open(StoreId storeId, UUID requestId) {
        Store store = storeFinder.find(storeId);

        checkOwner(requestId, store);

        store.open();

        storeRepository.save(store);
    }

    @Override
    public void forcedOpen(StoreId storeId) {
        Store store = storeFinder.find(storeId);

        store.open();

        storeRepository.save(store);
    }

    @Override
    public void close(StoreId storeId, UUID requestId) {
        Store store = storeFinder.find(storeId);

        checkOwner(requestId, store);

        store.close();

        storeRepository.save(store);
    }

    @Override
    public void forcedClose(StoreId storeId) {
        Store store = storeFinder.find(storeId);

        store.close();

        storeRepository.save(store);
    }

    @Override
    public void remove(StoreId storeId, UUID requestId) {
        Store store = storeFinder.find(storeId);

        checkOwner(requestId, store);

        store.remove();

        storeRepository.save(store);
    }

    @Override
    public void forcedRemove(StoreId storeId) {
        Store store = storeFinder.find(storeId);

        store.remove();

        storeRepository.save(store);
    }

    private void checkOwner(UUID requestId, Store store) {
        if (!store.getOwner().getId().equals(requestId)) {
            throw new IllegalArgumentException("가게 주인이 아닙니다.");
        }
    }
}
