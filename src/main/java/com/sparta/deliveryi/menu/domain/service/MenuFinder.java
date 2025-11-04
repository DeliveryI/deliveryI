package com.sparta.deliveryi.menu.domain.service;

import com.sparta.deliveryi.menu.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuFinder {

    Page<Menu> findMenusByStore(UUID targetStoreId,
                                String currentStoreId,
                                String role,
                                String menuName,
                                Pageable pageable);

    Menu findById(Long menuId);
}
