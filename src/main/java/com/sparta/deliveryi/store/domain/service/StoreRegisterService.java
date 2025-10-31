package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import com.sparta.deliveryi.store.domain.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreRegisterService implements StoreRegister {

    private final StoreRepository storeRepository;

    @Override
    public Store register(StoreRegisterRequest registerRequest) {
        Store store = Store.registerRequest(registerRequest);

        storeRepository.save(store);

        return store;
    }
}
