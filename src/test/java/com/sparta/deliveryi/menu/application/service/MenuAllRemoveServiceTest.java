package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import com.sparta.deliveryi.menu.domain.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("MenuAllRemoveService 테스트")
class MenuAllRemoveServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuAllRemoveService menuAllRemoveService;

    private UUID storeId;
    private String deletedByUsername;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storeId = UUID.randomUUID();
        deletedByUsername = "ownerUser";
    }

    @Test
    @DisplayName("특정 storeId의 모든 메뉴 삭제 ")
    void removeAllByStoreId_softDeleteMenus() {
        // given
        Menu menu1 = Menu.create(storeId, "짜장면", 8000, "맛있는 짜장면", MenuStatus.FORSALE, "owner1");
        Menu menu2 = Menu.create(storeId, "짬뽕", 9000, "얼큰한 짬뽕", MenuStatus.FORSALE, "owner1");

        List<Menu> menus = List.of(menu1, menu2);
        when(menuRepository.findAllByStoreIdAndDeletedAtIsNull(storeId)).thenReturn(menus);

        // when
        menuAllRemoveService.removeAllByStoreId(storeId, deletedByUsername);

        // then
        verify(menuRepository, times(1)).findAllByStoreIdAndDeletedAtIsNull(storeId);

        assertThat(menu1.getDeletedAt()).isNotNull();
        assertThat(menu1.getDeletedBy()).isEqualTo(deletedByUsername);
        assertThat(menu2.getDeletedAt()).isNotNull();
        assertThat(menu2.getDeletedBy()).isEqualTo(deletedByUsername);
    }

    @Test
    @DisplayName("삭제할 메뉴가 없을 경우 예외 없이 정상 처리")
    void removeAllByStoreId_emptyList_noError() {
        // given
        when(menuRepository.findAllByStoreIdAndDeletedAtIsNull(storeId)).thenReturn(List.of());

        // when & then
        assertDoesNotThrow(() -> menuAllRemoveService.removeAllByStoreId(storeId, deletedByUsername));
        verify(menuRepository, times(1)).findAllByStoreIdAndDeletedAtIsNull(storeId);
    }
}
