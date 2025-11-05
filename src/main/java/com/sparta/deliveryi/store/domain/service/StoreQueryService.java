package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.Owner;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreQueryService implements StoreFinder {

    private final StoreRepository storeRepository;

    @Override
    public Store find(StoreId storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게 정보를 찾을 수 없습니다. id: " + storeId));
    }

    @Override
    public List<Store> findByOwner(Owner owner) {
        return storeRepository.findAllByOwnerAndDeletedByIsNotNull(owner);
    }

}
