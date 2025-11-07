package com.sparta.deliveryi.menu.application.event;

import com.sparta.deliveryi.menu.application.service.MenuAllRemoveService;
import com.sparta.deliveryi.store.event.StoreRemoveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreRemoveEventHandler {

    private final MenuAllRemoveService menuAllRemoveService;

    @Async
    @EventListener(StoreRemoveEvent.class)
    public void handleStoreRemoveEvent(StoreRemoveEvent event) {
        menuAllRemoveService.removeAllByStoreId(event.storeId(), event.userId().toString());
    }
}
