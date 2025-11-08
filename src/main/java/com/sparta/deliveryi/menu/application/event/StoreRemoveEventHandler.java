package com.sparta.deliveryi.menu.application.event;

import com.sparta.deliveryi.menu.application.service.MenuAllRemoveService;
import com.sparta.deliveryi.store.event.StoreRemoveEvent;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class StoreRemoveEventHandler {

    private final MenuAllRemoveService menuAllRemoveService;
    private final UserQuery userQuery;

    @Async
    @TransactionalEventListener(StoreRemoveEvent.class)
    public void handleStoreRemoveEvent(StoreRemoveEvent event) {
        var user = userQuery.getUserById(UserId.of(event.userId()));

        String username = user.getUsername();

        menuAllRemoveService.removeAllByStoreId(event.storeId(), username);
    }
}
