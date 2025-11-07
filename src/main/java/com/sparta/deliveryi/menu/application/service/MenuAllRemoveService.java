package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuAllRemoveService {

    private final MenuRepository menuRepository;
    private final EntityManager entityManager;

    @Transactional
    public void removeAllByStoreId(UUID storeId, String deletedBy) {
        var menus = menuRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);

        log.info("menus size = {}", menus.size());

        menus.forEach(menu -> {
            log.info("menu name = {}", menu.getMenuName());
            menu.markDeleted(deletedBy);
            Menu result = menuRepository.save(menu);
            entityManager.flush();
            entityManager.clear();
            log.info("finish = {}", result.getDeletedBy());
        });
    }
}
