package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuAllRemoveService {

    private final MenuRepository menuRepository;

    @Transactional
    public void removeAllByStoreId(UUID storeId, String deletedBy) {
        var menus = menuRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);
        menus.forEach(menu -> menu.markDeleted(deletedBy));
    }
}
