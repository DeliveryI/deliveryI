package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
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

    @Override
    public Store register(StoreRegisterRequest registerRequest) {
        Store store = Store.registerRequest(registerRequest);

        storeRepository.save(store);

        return store;
    }
}
