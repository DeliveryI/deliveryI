package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.service.MenuFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuQueryService {

    private final MenuFinder menuFinder;

    public Page<Menu> getMenusByStore(
            UUID targetStoreId,
            UUID currentStoreId,
            String role,
            String menuName,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return menuFinder.findMenusByStore(targetStoreId, currentStoreId, role, menuName, pageable);
    }

    public Menu getMenu(Long menuId) {
        return menuFinder.findById(menuId);
    }
}
