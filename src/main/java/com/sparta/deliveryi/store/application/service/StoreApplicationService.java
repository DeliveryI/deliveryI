package com.sparta.deliveryi.store.application.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreInfoUpdateRequest;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.store.domain.service.StoreManager;
import com.sparta.deliveryi.store.domain.service.StoreRegister;
import com.sparta.deliveryi.user.application.service.UserRolePolicy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreApplicationService implements StoreApplication {

    private final StoreRegister storeRegister;

    private final StoreManager storeManager;

    private final StoreFinder storeFinder;

    private final UserRolePolicy userRolePolicy;

    @Override
    public Store updateInfo(UUID storeId, StoreInfoUpdateRequest updateRequest, UUID requestId) {
        StoreId id = StoreId.of(storeId);

        if (isAdmin(requestId)) {
            return storeManager.forcedUpdateInfo(id, updateRequest);
        }

        return storeManager.updateInfo(id, updateRequest, requestId);
    }

    @Override
    public Store remove(UUID storeId, UUID requestId) {
        return removeStore(StoreId.of(storeId), requestId);
    }

    @Override
    public void open(UUID storeId, UUID requestId) {
        StoreId id = StoreId.of(storeId);

        if (isAdmin(requestId)) {
            storeManager.forcedOpen(id);
            return;
        }

        storeManager.open((StoreId.of(storeId)), requestId);
    }

    @Override
    public void close(UUID storeId, UUID requestId) {
        StoreId id = StoreId.of(storeId);

        if (isAdmin(requestId)) {
            storeManager.forcedClose(id);
            return;
        }

        storeManager.close((StoreId.of(storeId)), requestId);
    }

    @Override
    public void transfer(UUID storeId, UUID newOwnerId, UUID requestId) {
        StoreId id = StoreId.of(storeId);
        UUID oldOwnerId = resolveOldOwnerId(id, requestId);

        storeManager.transfer(id, newOwnerId, oldOwnerId);
    }

    private boolean isAdmin(UUID requestId) {
        return userRolePolicy.isAdmin(requestId);
    }

    private Store removeStore(StoreId storeId, UUID requestId) {
        if (isAdmin(requestId)) {
            return storeManager.forcedRemove(storeId);
        }

        return storeManager.remove(storeId, requestId);
    }

    private UUID resolveOldOwnerId(StoreId storeId, UUID requestId) {
        if (isAdmin(requestId)) {
            Store store = storeFinder.find(storeId);
            return store.getOwner().getId();
        }

        return requestId;
    }

}
