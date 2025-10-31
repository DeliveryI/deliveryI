package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreRegisterRequest;
import jakarta.validation.Valid;

public interface StoreRegister {
    Store register(@Valid StoreRegisterRequest registerRequest);
}
