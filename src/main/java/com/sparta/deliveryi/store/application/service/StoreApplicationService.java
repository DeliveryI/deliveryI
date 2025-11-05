package com.sparta.deliveryi.store.application.service;

import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.store.domain.*;
import com.sparta.deliveryi.store.domain.event.StoreRegisterEvent;
import com.sparta.deliveryi.store.domain.event.StoreRemoveEvent;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.store.domain.service.StoreManager;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import com.sparta.deliveryi.user.application.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreApplicationService implements StoreApplication {

    private final StoreRegister storeRegister;

    private final StoreManager storeManager;

    private final StoreFinder storeFinder;

    private final UserRoleService userRoleService;

    @Override
    public Store register(StoreRegisterRequest registerRequest, UUID requestId) {
        Store store = storeRegister.register(registerRequest);

        if (userRoleService.isCustomer(requestId)) {
            Events.trigger(new StoreRegisterEvent(requestId));
        }

        return store;
    }

    @Override
    public Store updateInfo(UUID storeId, StoreInfoUpdateRequest updateRequest, UUID requestId) {
        StoreId id = StoreId.of(storeId);

        if (userRoleService.isAdmin(requestId)) {
            return storeManager.forcedUpdateInfo(id, updateRequest);
        }

        return storeManager.updateInfo(id, updateRequest, requestId);
    }

    @Override
    public Store remove(UUID storeId, UUID requestId) {
        StoreId id = StoreId.of(storeId);

        Store store = isAdmin(requestId)
                ? removeStoreAsAdmin(id)
                : removeStoreAsOwner(id, requestId);

        triggerEventIfLastStore(store.getOwner(), requestId);

        return store;
    }

    @Override
    public void open(UUID storeId, UUID requestId) {
        StoreId id = StoreId.of(storeId);

        if (userRoleService.isAdmin(requestId)) {
            storeManager.forcedOpen(id);
            return;
        }

        storeManager.open((StoreId.of(storeId)), requestId);
    }

    @Override
    public void close(UUID storeId, UUID requestId) {
        StoreId id = StoreId.of(storeId);

        if (userRoleService.isAdmin(requestId)) {
            storeManager.forcedClose(id);
            return;
        }

        storeManager.close((StoreId.of(storeId)), requestId);
    }

    private boolean isAdmin(UUID requestId) {
        return userRoleService.isAdmin(requestId);
    }

    private Store removeStoreAsAdmin(StoreId storeId) {
        return storeManager.forcedRemove(storeId);
    }

    private Store removeStoreAsOwner(StoreId storeId, UUID requestId) {
        return storeManager.remove(storeId, requestId);
    }

    private void triggerEventIfLastStore(Owner owner, UUID requestId) {
        List<Store> remainingStores = storeFinder.findByOwner(owner);

        if (remainingStores.isEmpty()) {
            Events.trigger(new StoreRemoveEvent(requestId));
        }
    }
}
