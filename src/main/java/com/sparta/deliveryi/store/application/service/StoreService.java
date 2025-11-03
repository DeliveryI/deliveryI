package com.sparta.deliveryi.store.application.service;

import com.sparta.deliveryi.store.domain.StoreId;
import com.sparta.deliveryi.store.domain.service.StoreManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreManager storeManager;

    public void open(UUID storeId, UUID requestId) {
//        TODO 권한 확인 후 다른 메서드 호출
//        ex)
//        User user = userFinder.find(requestId);
//
//        if(user.isManager()){
//            storeManager.forcedOpen((StoreId.of(storeId));
//            return;
//        }

        storeManager.open((StoreId.of(storeId)), requestId);
    }
}
