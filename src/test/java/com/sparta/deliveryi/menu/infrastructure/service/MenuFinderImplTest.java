package com.sparta.deliveryi.menu.infrastructure.service;

import com.sparta.deliveryi.global.infrastructure.config.QueryDslConfig;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QueryDslConfig.class, MenuFinderImpl.class})
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

        // ✅ createdAt, updatedAt 하드코딩으로 넣어줌
        LocalDateTime now = LocalDateTime.now();
        setAuditFields(m1, now);
        setAuditFields(m2, now);
        setAuditFields(m3, now);

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.flush();
        em.clear();
    }

    private void setAuditFields(Menu menu, LocalDateTime now) {
        try {
            var createdAt = menu.getClass().getSuperclass().getDeclaredField("createdAt");
            var updatedAt = menu.getClass().getSuperclass().getDeclaredField("updatedAt");
            var updatedBy = menu.getClass().getSuperclass().getDeclaredField("updatedBy");

            createdAt.setAccessible(true);
            updatedAt.setAccessible(true);
            updatedBy.setAccessible(true);

            createdAt.set(menu, now);
            updatedAt.set(menu, now);
            updatedBy.set(menu, "owner");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("가게 ID로 메뉴 목록 조회 성공")
    void findMenusByStore_success() {
        Page<Menu> result = menuFinder.findMenusByStore(
                storeId,
                storeId,
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
}
