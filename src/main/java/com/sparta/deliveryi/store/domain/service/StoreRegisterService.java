package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
    public void acceptRegisterRequest(StoreId storeId) {
        Store store = storeFinder.find(storeId);

        store.acceptRegisterRequest();

        storeRepository.save(store);
    }

    @Override
    public void rejectRegisterRequest(StoreId storeId) {
        Store store = storeFinder.find(storeId);

        store.rejectRegisterRequest();

        storeRepository.save(store);
    }


}
