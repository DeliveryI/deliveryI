package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.StoreRepository;
import com.sparta.deliveryi.store.event.StoreRemoveEvent;
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
    public Store updateInfo(StoreId storeId, StoreInfoUpdateRequest updateRequest, UUID requestId) {
        Store store = storeFinder.find(storeId);

        checkOwner(requestId, store);

        store.updateInfo(updateRequest);

        return storeRepository.save(store);
    }

    @Override
    public Store forcedUpdateInfo(StoreId storeId, StoreInfoUpdateRequest updateRequest) {
        Store store = storeFinder.find(storeId);

        store.updateInfo(updateRequest);

        return storeRepository.save(store);
    }

    @Override
    public Store remove(StoreId storeId, UUID requestId) {
        Store store = storeFinder.find(storeId);

        checkOwner(requestId, store);

        store.remove();

        store = storeRepository.save(store);

        Events.trigger(new StoreRemoveEvent(store.getOwner().getId(), requestId));

        return store;
    }

    @Override
    public Store forcedRemove(StoreId storeId) {
        Store store = storeFinder.find(storeId);

        store.remove();

        return storeRepository.save(store);
    }

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
    public void transfer(StoreId storeId, UUID newOwnerId, UUID requestId) {
        Store store = storeFinder.find(storeId);

        checkOwner(requestId, store);

        store.transferOwnership(newOwnerId);

        storeRepository.save(store);
    }

    private void checkOwner(UUID requestId, Store store) {
        if (!store.getOwner().getId().equals(requestId)) {
            throw new IllegalArgumentException("가게 주인이 아닙니다.");
        }
    }
}
