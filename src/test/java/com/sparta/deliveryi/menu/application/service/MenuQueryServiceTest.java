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
    @DisplayName("getMenu (권한 정책 적용): OWNER가 자신의 가게 메뉴 조회 성공")
    void getMenu_withVisibility_success() {
        // given
        Long menuId = 1L;
        UUID storeId = UUID.randomUUID();
        UUID currentStoreId = storeId;
        String role = "OWNER";

        Menu menu = Menu.create(storeId, "돈까스", 9000, "바삭한 돈까스", MenuStatus.FORSALE, "ownerUser");

        given(menuFinder.findMenuByIdWithVisibility(
                eq(menuId),
                eq(storeId),
                eq(currentStoreId),
                eq(role)
        )).willReturn(menu);

        // when
        Menu result = menuQueryService.getMenu(menuId, storeId, currentStoreId, role);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMenuName()).isEqualTo("돈까스");
        assertThat(result.getMenuPrice()).isEqualTo(9000);
        assertThat(result.getCreatedBy()).isEqualTo("ownerUser");
    }
}
