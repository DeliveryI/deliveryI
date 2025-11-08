package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.service.MenuFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuQueryService {

    private final MenuFinder menuFinder;

    private static final List<Integer> ALLOWED_PAGE_SIZES = List.of(10, 30, 50);
    private static final int DEFAULT_PAGE_SIZE = 10;

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
        int validatedSize = ALLOWED_PAGE_SIZES.contains(size) ? size : DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(Sort.Direction.fromString(direction), sortBy));

        return menuFinder.findMenusByStore(targetStoreId, currentStoreId, role, menuName, pageable);
    }

    public Menu getMenu(Long menuId, UUID targetStoreId, UUID currentStoreId, String role) {
        return menuFinder.findMenuByIdWithVisibility(menuId, targetStoreId, currentStoreId, role);
    }
}
