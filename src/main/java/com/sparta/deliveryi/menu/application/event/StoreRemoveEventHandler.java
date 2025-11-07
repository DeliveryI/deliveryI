package com.sparta.deliveryi.menu.application.event;

import com.sparta.deliveryi.menu.application.service.MenuAllRemoveService;
import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import com.sparta.deliveryi.store.event.StoreRemoveEvent;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreRemoveEventHandler {

    private final MenuRepository menuRepository;
    private final UserQuery userQuery;

    @Async
    @TransactionalEventListener(StoreRemoveEvent.class)
    public void handleStoreRemoveEvent(StoreRemoveEvent event) {
        var user = userQuery.getUserById(UserId.of(event.userId()));

        String username = user.getUsername();

        var menus = menuRepository.findAllByStoreIdAndDeletedAtIsNull(event.storeId());

        log.info("menus size = {}", menus.size());

        menus.forEach(menu -> {
            log.info("menu name = {}", menu.getMenuName());
            menu.markDeleted(username);
            Menu result = menuRepository.save(menu);
            log.info("finish = {}", result.getDeletedBy());
        });
//        menuAllRemoveService.removeAllByStoreId(event.storeId(), username);
    }
}
