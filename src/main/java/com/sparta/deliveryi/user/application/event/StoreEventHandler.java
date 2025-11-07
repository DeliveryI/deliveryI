package com.sparta.deliveryi.user.application.event;

import com.sparta.deliveryi.store.domain.Owner;
import com.sparta.deliveryi.store.domain.Store;
import com.sparta.deliveryi.store.event.StoreRegisterAcceptEvent;
import com.sparta.deliveryi.store.event.StoreRemoveEvent;
import com.sparta.deliveryi.store.event.StoreTransferEvent;
import com.sparta.deliveryi.store.domain.service.StoreFinder;
import com.sparta.deliveryi.user.application.dto.AuthUser;
import com.sparta.deliveryi.user.application.service.AuthApplication;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.UserRole;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import com.sparta.deliveryi.user.domain.service.UserUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreEventHandler {

    private final UserQuery userQuery;
    private final UserUpdate userUpdate;
    private final AuthApplication authApplication;

    private final StoreFinder storeFinder;

    @Async
    @TransactionalEventListener(StoreRegisterAcceptEvent.class)
    public void handleStoreRegisterEvent(StoreRegisterAcceptEvent event) {
        changeToOwner(event.userId(), event.updatedBy());
    }

    @Async
    @TransactionalEventListener(StoreRemoveEvent.class)
    public void handleStoreRemoveEvent(StoreRemoveEvent event) {
        changeToCustomer(event.userId());
    }

    @Async
    @TransactionalEventListener(StoreTransferEvent.class)
    public void handleStoreTransferEvent(StoreTransferEvent event) {
        changeToCustomer(event.ownerId());
        changeToOwner(event.newOwnerId(), null);
    }

    private void changeToOwner(UUID userId, UUID requestId) {
        UserRole role = getRoleByUserId(userId);

        if (role == UserRole.CUSTOMER) {
            String updatedBy = requestId != null
                    ? userQuery.getUserById(UserId.of(requestId)).getUsername()
                    : "SYSTEM";
            updateRole(userId, UserRole.OWNER, updatedBy);
        }
    }

    private void changeToCustomer(UUID userId) {
        // 1. 등록된 가게가 있는지
        List<Store> stores = storeFinder.findByOwner(Owner.of(userId));
        if (!stores.isEmpty()) return;

        // 2. 역할이 OWNER 인지
        UserRole role = getRoleByUserId(userId);

        if (role == UserRole.OWNER) {
            String updatedBy = userQuery.getUserById(UserId.of(userId)).getUsername();
            updateRole(userId, UserRole.CUSTOMER, updatedBy);
        }
    }

    private UserRole getRoleByUserId(UUID userId) {
        // DB에서 user 조회
        User user = userQuery.getUserById(UserId.of(userId));

        // Keycloak에서 role 조회
        AuthUser keycloakUser = authApplication.getUserById(user.getKeycloakId().toUuid());

        return keycloakUser.role();
    }

    private void updateRole(UUID userId, UserRole role, String updatedBy) {
        authApplication.updateRole(userId, role);
        userUpdate.updateRole(UserId.of(userId), role, updatedBy);
    }
}
