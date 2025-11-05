package com.sparta.deliveryi.menu.infrastructure.cilent;

import com.sparta.deliveryi.ai.domain.service.MenuLookupClient;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuLookupClientImpl implements MenuLookupClient {

    private final MenuRepository menuRepository;

    @Override
    public boolean existsMenuById(Long menuId) {
        return menuRepository.existsById(menuId);
    }
}
