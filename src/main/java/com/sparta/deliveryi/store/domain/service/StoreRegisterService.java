package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.global.infrastructure.event.Events;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.StoreRepository;
import com.sparta.deliveryi.store.event.StoreRegisterAcceptEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class StoreRegisterService implements StoreRegister {

    private final StoreRepository storeRepository;

    private final StoreFinder storeFinder;

    @Override
    public Store register(StoreRegisterRequest registerRequest) {
        Store store = Store.registerRequest(registerRequest);

        storeRepository.save(store);

        return store;
    }

    @Override
    public void acceptRegisterRequest(UUID storeId, UUID requestId) {
        Store store = storeFinder.find(StoreId.of(storeId));

        store.acceptRegisterRequest();

        storeRepository.save(store);

        Events.trigger(new StoreRegisterAcceptEvent(store.getOwner().getId(), requestId));
    }

    @Override
    public void rejectRegisterRequest(UUID storeId) {
        Store store = storeFinder.find(StoreId.of(storeId));

        store.rejectRegisterRequest();

        storeRepository.save(store);
    }


}
