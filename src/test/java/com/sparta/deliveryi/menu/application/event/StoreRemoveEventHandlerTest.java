package com.sparta.deliveryi.menu.application.event;

import com.sparta.deliveryi.menu.application.service.MenuAllRemoveService;
import com.sparta.deliveryi.store.event.StoreRemoveEvent;
import com.sparta.deliveryi.user.domain.User;
import com.sparta.deliveryi.user.domain.UserId;
import com.sparta.deliveryi.user.domain.service.UserQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;

class StoreRemoveEventHandlerTest {

    @Mock
    private MenuAllRemoveService menuAllRemoveService;

    @Mock
    private UserQuery userQuery;

    @InjectMocks
    private StoreRemoveEventHandler handler;

    private UUID storeId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storeId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("StoreRemoveEvent 발생 시 해당 사용자의 username으로 메뉴 일괄 삭제를 수행한다")
    void handleStoreRemoveEvent_success() {
        // given
        var event = new StoreRemoveEvent(storeId, userId);

        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("testUser");
        when(userQuery.getUserById(UserId.of(userId))).thenReturn(mockUser);

        // when
        handler.handleStoreRemoveEvent(event);

        // then
        verify(userQuery, times(1)).getUserById(UserId.of(userId));
        verify(menuAllRemoveService, times(1))
                .removeAllByStoreId(storeId, "testUser");
    }
}
