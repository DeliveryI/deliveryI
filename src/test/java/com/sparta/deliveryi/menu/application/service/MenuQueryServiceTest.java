package com.sparta.deliveryi.menu.application.service;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import com.sparta.deliveryi.menu.domain.service.MenuFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuQueryService 단위 테스트")
class MenuQueryServiceTest {

    @Mock
    private MenuFinder menuFinder;

    @InjectMocks
    private MenuQueryService menuQueryService;

    @Test
    @DisplayName("getMenusByStore: 가게별 메뉴 목록 조회 성공")
    void getMenusByStore_success() {
        // given
        UUID storeId = UUID.randomUUID();
        UUID ownerStoreId = storeId;
        String role = "OWNER";
        String menuName = "라면";

        Menu menu = Menu.create(storeId, "라면", 6000, "간단한 식사", MenuStatus.FORSALE, "tester");
        Page<Menu> expectedPage = new PageImpl<>(List.of(menu));

        given(menuFinder.findMenusByStore(
                eq(storeId),
                eq(ownerStoreId),
                eq(role),
                eq(menuName),
                any(Pageable.class)
        )).willReturn(expectedPage);

        // when
        Page<Menu> result = menuQueryService.getMenusByStore(
                storeId,
                ownerStoreId,
                role,
                menuName,
                0,
                10,
                "createdAt",
                "DESC"
        );

        // then
        assertThat(result).hasSize(1);
        Menu found = result.getContent().getFirst();
        assertThat(found.getMenuName()).isEqualTo("라면");
        assertThat(found.getMenuPrice()).isEqualTo(6000);
    }

    @Test
    @DisplayName("getMenu: 단일 메뉴 조회 성공")
    void getMenu_success() {
        // given
        Menu menu = Menu.create(UUID.randomUUID(), "김치찌개", 8000, "따뜻한 찌개", MenuStatus.FORSALE, "tester");
        given(menuFinder.findById(anyLong())).willReturn(menu);

        // when
        Menu result = menuQueryService.getMenu(1L);

        // then
        assertThat(result.getMenuName()).isEqualTo("김치찌개");
        assertThat(result.getMenuPrice()).isEqualTo(8000);
        assertThat(result.getCreatedBy()).isEqualTo("tester");
    }
}
