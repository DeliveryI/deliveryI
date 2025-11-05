package com.sparta.deliveryi.menu.infrastructure.service;

import com.sparta.deliveryi.menu.domain.Menu;
import com.sparta.deliveryi.menu.domain.MenuStatus;
import com.sparta.deliveryi.menu.domain.service.MenuFinder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({com.sparta.deliveryi.global.infrastructure.config.QueryDslConfig.class,
        com.sparta.deliveryi.menu.infrastructure.service.MenuFinderImpl.class})
@DisplayName("MenuFinderImpl 통합 테스트")
class MenuFinderImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MenuFinder menuFinder;

    private UUID storeId;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();

        Menu m1 = Menu.create(storeId, "비빔밥", 9000, "한식 대표 메뉴", MenuStatus.FORSALE, "owner");
        Menu m2 = Menu.create(storeId, "불고기", 11000, "정통 불고기", MenuStatus.FORSALE, "owner");
        Menu m3 = Menu.create(storeId, "전복죽", 12000, "보양식 메뉴", MenuStatus.HIDING, "owner");

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("가게 ID로 메뉴 목록 조회 성공")
    void findMenusByStore_success() {
        Page<Menu> result = menuFinder.findMenusByStore(
                storeId,
                storeId.toString(),
                "OWNER",
                null,
                PageRequest.of(0, 10)
        );

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .extracting(Menu::getMenuName)
                .containsExactlyInAnyOrder("비빔밥", "불고기", "전복죽");
    }

    @Test
    @DisplayName("메뉴 이름 필터링으로 조회 성공")
    void findMenusByStore_withMenuNameFilter() {
        Page<Menu> result = menuFinder.findMenusByStore(
                storeId,
                storeId.toString(),
                "OWNER",
                "비빔",
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getMenuName()).isEqualTo("비빔밥");
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 ID 조회 시 null 반환")
    void findById_notFound() {
        Menu result = menuFinder.findById(999L);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("정상적으로 메뉴 ID로 조회 성공")
    void findById_success() {
        Long menuId = em.createQuery("SELECT m.menuId FROM Menu m", Long.class)
                .setMaxResults(1)
                .getSingleResult();

        Menu found = menuFinder.findById(menuId);

        assertThat(found).isNotNull();
        assertThat(found.getMenuId()).isEqualTo(menuId);
    }
}
