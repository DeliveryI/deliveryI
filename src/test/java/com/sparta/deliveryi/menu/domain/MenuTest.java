package com.sparta.deliveryi.menu.domain;

import com.sparta.deliveryi.TestMessageResolverInitializer;
import com.sparta.deliveryi.menu.domain.exception.MenuCreatedByEmptyException;
import com.sparta.deliveryi.menu.domain.exception.MenuDeletedException;
import com.sparta.deliveryi.menu.domain.exception.MenuPriceInvalidException;
import com.sparta.deliveryi.menu.domain.exception.MenuUpdatedByEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Menu 도메인 단위 테스트")
class MenuTest {

    @BeforeEach
    void setUp() throws Exception {
        storeId = UUID.randomUUID();
        creator = "storeOwner";
        updater = "manager";

        TestMessageResolverInitializer.initializeFromResourceBundle();
    }

    private UUID storeId;
    private String creator;
    private String updater;



    @Nested
    @DisplayName("메뉴 생성 테스트")
    class CreateMenu {

        @Test
        @DisplayName("정상적으로 메뉴 생성 성공")
        void createMenuSuccess() {
            Menu menu = Menu.create(storeId, "비빔밥", 9000, "한식 대표 메뉴", MenuStatus.FORSALE, creator);

            assertThat(menu.getStoreId()).isEqualTo(storeId);
            assertThat(menu.getMenuName()).isEqualTo("비빔밥");
            assertThat(menu.getMenuPrice()).isEqualTo(9000);
            assertThat(menu.getMenuDescription()).isEqualTo("한식 대표 메뉴");
            assertThat(menu.getMenuStatus()).isEqualTo(MenuStatus.FORSALE);
            assertThat(menu.getCreatedBy()).isEqualTo(creator);
            assertThat(menu.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("가격이 0원 이하일 경우 예외 발생")
        void createMenuInvalidPrice() {
            assertThatThrownBy(() ->
                    Menu.create(storeId, "비빔밥", 0, "한식", MenuStatus.FORSALE, creator)
            ).isInstanceOf(MenuPriceInvalidException.class);
        }

        @Test
        @DisplayName("생성자(createdBy)가 비어있을 경우 예외 발생")
        void createMenuWithoutCreatedBy() {
            assertThatThrownBy(() ->
                    Menu.create(storeId, "비빔밥", 8000, "한식", MenuStatus.FORSALE, " ")
            ).isInstanceOf(MenuCreatedByEmptyException.class);
        }
    }

    @Nested
    @DisplayName("메뉴 수정 테스트")
    class UpdateMenu {

        private Menu menu;

        @BeforeEach
        void initMenu() {
            menu = Menu.create(storeId, "불고기", 12000, "한국 전통 불고기", MenuStatus.FORSALE, creator);
        }

        @Test
        @DisplayName("정상적으로 메뉴 수정 성공")
        void updateMenuSuccess() {
            menu.update("한우불고기", 15000, "고급 한우 사용", MenuStatus.FORSALE, updater);

            assertThat(menu.getMenuName()).isEqualTo("한우불고기");
            assertThat(menu.getMenuPrice()).isEqualTo(15000);
            assertThat(menu.getMenuDescription()).isEqualTo("고급 한우 사용");
            assertThat(menu.getUpdatedBy()).isEqualTo(updater);
        }

        @Test
        @DisplayName("가격이 0원 이하일 경우 예외 발생")
        void updateMenuInvalidPrice() {
            assertThatThrownBy(() ->
                    menu.update("불고기", 0, "한국 전통 불고기", MenuStatus.FORSALE, updater)
            ).isInstanceOf(MenuPriceInvalidException.class);
        }

        @Test
        @DisplayName("수정자(updatedBy)가 비어있을 경우 예외 발생")
        void updateMenuWithoutUpdater() {
            assertThatThrownBy(() ->
                    menu.update("불고기", 12000, "한국 전통 불고기", MenuStatus.FORSALE, "")
            ).isInstanceOf(MenuUpdatedByEmptyException.class);
        }
    }

    @Nested
    @DisplayName("메뉴 상태 변경 테스트")
    class ChangeStatus {

        private Menu menu;

        @BeforeEach
        void initMenu() {
            menu = Menu.create(storeId, "전복죽", 10000, "영양가득 전복죽", MenuStatus.FORSALE, creator);
        }

        @Test
        @DisplayName("정상적으로 메뉴 상태 변경 성공")
        void changeStatusSuccess() {
            menu.changeStatus(MenuStatus.HIDING, updater);

            assertThat(menu.getMenuStatus()).isEqualTo(MenuStatus.HIDING);
            assertThat(menu.getUpdatedBy()).isEqualTo(updater);
        }

        @Test
        @DisplayName("동일한 상태로 변경 시 아무 동작 없음")
        void changeStatusNoChangeWhenSame() {
            menu.changeStatus(MenuStatus.FORSALE, updater);

            assertThat(menu.getMenuStatus()).isEqualTo(MenuStatus.FORSALE);
        }

        @Test
        @DisplayName("삭제된 메뉴의 상태 변경 시 예외 발생")
        void changeStatusWhenDeleted() {
            menu.delete();

            assertThatThrownBy(() ->
                    menu.changeStatus(MenuStatus.HIDING, updater)
            ).isInstanceOf(MenuDeletedException.class);
        }
    }

    @Nested
    @DisplayName("상태 확인 및 삭제 테스트")
    class VisibilityAndDeletion {

        private Menu menu;

        @BeforeEach
        void initMenu() {
            menu = Menu.create(storeId, "냉면", 9000, "여름별미 냉면", MenuStatus.FORSALE, creator);
        }

        @Test
        @DisplayName("HIDING 상태가 아니면 고객에게 노출됨")
        void visibleToCustomer() {
            assertThat(menu.isVisibleToCustomer()).isTrue();
        }

        @Test
        @DisplayName("HIDING 상태면 고객에게 노출되지 않음")
        void hiddenFromCustomer() {
            menu.changeStatus(MenuStatus.HIDING, updater);
            assertThat(menu.isVisibleToCustomer()).isFalse();
        }

        @Test
        @DisplayName("delete() 호출 시 isDeleted() true 반환")
        void deleteMenu() {
            menu.delete();
            assertThat(menu.isDeleted()).isTrue();
        }
    }
}
