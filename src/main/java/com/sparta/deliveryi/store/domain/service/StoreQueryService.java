package com.sparta.deliveryi.store.domain.service;

import com.sparta.deliveryi.store.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return storeRepository.findAllByOwnerAndDeletedAtIsNull(owner);
    }

    @Override
    public Page<Store> search(StoreSearchCondition searchCondition) {
        return storeRepository.search(
                searchCondition.owner(),
                searchCondition.keyword(),
                searchCondition.includeDeleted(),
                searchCondition.pageable()
        );
    }

}
